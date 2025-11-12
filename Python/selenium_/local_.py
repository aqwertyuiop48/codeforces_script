import unittest, os
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait, Select
from selenium.webdriver.support import expected_conditions as EC
from webdriver_manager.chrome import ChromeDriverManager

html_file = os.path.abspath("Python/selenium_/index.html")  # full path

class MockUITest(unittest.TestCase):

    def setUp(self):
        chrome_options = Options()
        chrome_options.add_argument("--headless")  # comment to see browser
        chrome_options.add_argument("--disable-gpu")
        chrome_options.add_argument("--no-sandbox")
        chrome_options.add_argument("--window-size=1920,1080")
        self.driver = webdriver.Chrome(
            service=Service(ChromeDriverManager().install()),
            options=chrome_options
        )

    def tearDown(self):
        self.driver.quit()

    def test_form_interactions(self):
        self.driver.get(f"file://{html_file}")

        wait = WebDriverWait(self.driver, 10)

        # Fill inputs
        wait.until(EC.presence_of_element_located((By.ID, "name"))).send_keys("Alice")

        # Check checkbox
        wait.until(EC.element_to_be_clickable((By.ID, "subscribe"))).click()

        # Select radio
        wait.until(EC.element_to_be_clickable((By.ID, "female"))).click()

        # Select dropdown
        select_elem = wait.until(EC.presence_of_element_located((By.ID, "country")))
        Select(select_elem).select_by_value("in")

        # Click submit
        wait.until(EC.element_to_be_clickable((By.ID, "submit-btn"))).click()

        # Assert result text
        result_text = wait.until(EC.presence_of_element_located((By.ID, "result"))).text
        expected_text = "Name: Alice, Gender: female, Country: in, Subscribed: true"
        self.assertEqual(result_text, expected_text)

    def test_link_clickable(self):
        self.driver.get(f"file://{html_file}")

        wait = WebDriverWait(self.driver, 10)
        link = wait.until(EC.element_to_be_clickable((By.ID, "learn-more")))

        self.assertTrue(link.is_enabled())

if __name__ == "__main__":
    unittest.main()
