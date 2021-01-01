###Dependencies:
- Java 11
- Gradle
- Chrome ver. 87
---
To run the project simply run `gradle test` from the root folder

---
After running the project, reports will be generated in `root/reports`

### Coverage
The tests are capable of searching for any two items on etsy.com pre-defined with keywords in the feature file, and adding them to the user's cart.
Currently the first item is always added based on its price being the highest possible. The second item is picked randomly from the first page of search results.

### Known issues
- The tests are not configured to handle a search result that doesn't come up with any entries
- Occasionally the tests sort by popularity instead of descending price order, however a while loop ensures that the correct sorting is selected in the end

### Troubleshooting

- In case of issues with webdriver itself, replace `src/test/resources/chromedriver_win32/chromedriver.exe`
with a version corresponding with your browser's version from [here](https://chromedriver.storage.googleapis.com/index.html)
