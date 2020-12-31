###Dependencies:
- Java 11
- Gradle
- Chrome ver. 87
---
To run the project simply run `gradle test` from the root folder

---
After running the project, reports will be generated in `root/reports`

### Troubleshooting

- In case of issues with webdriver itself, replace `src/test/resources/chromedriver_win32/chromedriver.exe`
with a version corresponding with your browser's version from [here](https://chromedriver.storage.googleapis.com/index.html)