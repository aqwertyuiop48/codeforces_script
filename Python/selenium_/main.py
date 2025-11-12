import unittest
from selenium import webdriver
from selenium.webdriver.common.by import By
from selenium.webdriver.chrome.service import Service
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.support.ui import WebDriverWait
from selenium.webdriver.support import expected_conditions as EC
from webdriver_manager.chrome import ChromeDriverManager

class SeleniumEasyTest(unittest.TestCase):

    def setUp(self):
        chrome_options = Options()
        chrome_options.add_argument("--headless")  # comment out to see browser
        chrome_options.add_argument("--disable-gpu")
        chrome_options.add_argument("--no-sandbox")
        chrome_options.add_argument("--window-size=1920,1080")
        self.driver = webdriver.Chrome(
            service=Service(ChromeDriverManager().install()),
            options=chrome_options
        )

    def tearDown(self):
        self.driver.quit()

    def test_download_progress_button(self):
        self.driver.get("https://www.seleniumeasy.com/test/jquery-download-progress-bar-demo.html")

        wait = WebDriverWait(self.driver, 30)
        download_button = wait.until(
            EC.element_to_be_clickable((By.ID, "downloadButton"))
        )
        download_button.click()

        # Optional: wait for completion message
        complete_text = wait.until(
            EC.visibility_of_element_located((By.CLASS_NAME, "progress-label"))
        ).text

        self.assertIn("Complete!", complete_text)


if __name__ == "__main__":
    unittest.main()
