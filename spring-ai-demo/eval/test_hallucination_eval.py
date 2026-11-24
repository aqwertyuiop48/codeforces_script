"""LLM reliability evals against a RUNNING app (pytest).

    pip install pytest requests
    BASE_URL=http://localhost:8080 pytest -q eval/

Needs a configured API key; every test is skipped if the app is unreachable or has no key.
The point of an eval suite: a fixed set of questions with known answers (and known traps) that you re-run on every
prompt/model change to catch regressions, e.g. the model starting to invent facts the knowledge base does not hold.
"""
import os
import unittest

import pytest
import requests

BASE = os.environ.get("BASE_URL", "http://localhost:8080")


def ask(question):
    return requests.get(f"{BASE}/api/ask", params={"question": question}, timeout=120)


@pytest.fixture(scope="module", autouse=True)
def app_ready():
    try:
        models = requests.get(f"{BASE}/api/models", timeout=5).json()
    except requests.RequestException:
        pytest.skip("app not reachable")
    if not any(m["configured"] for m in models):
        pytest.skip("no API key configured")


@pytest.mark.parametrize("question, must_contain, source", [
    ("How many days do I have to return an item?", "30", "returns-policy"),
    ("How long does standard shipping take?", "5", "shipping"),
    ("How long is the warranty on electronics?", "12", "warranty"),
])
def test_answers_are_grounded_in_the_knowledge_base(question, must_contain, source):
    body = ask(question).json()
    assert body["grounded"] is True
    assert must_contain in body["answer"]
    assert source in body["sources"]


@pytest.mark.parametrize("question", [
    "Who won the football world cup in 2018?",
    "What is the CEO's phone number?",
])
def test_refuses_to_invent_facts_outside_the_knowledge_base(question):
    body = ask(question).json()
    assert body["grounded"] is False
    assert "don't know" in body["answer"].lower() or "do not know" in body["answer"].lower()


class ApiContractTest(unittest.TestCase):
    """unittest style, for the JD's 'PyTest, Unittest'."""

    def test_models_endpoint_lists_both_providers(self):
        try:
            models = requests.get(f"{BASE}/api/models", timeout=5).json()
        except requests.RequestException:
            self.skipTest("app not reachable")
        self.assertEqual({"openai", "gemini"}, {m["provider"] for m in models})
