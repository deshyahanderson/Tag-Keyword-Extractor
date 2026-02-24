# Tag/Keyword Extractor: Computational Linguistics and Data Processing

## 💡 Overview
This project is a Java-based data processing tool designed to perform keyword extraction and frequency analysis on large text datasets. By leveraging **Computational Linguistics** principles, the application filters out "noise words" (stop words) to identify the most significant tags within a document. The core challenge involves efficient text normalization and the strategic use of **Abstract Data Types (ADTs)** to manage high-volume data lookups.



## 🎯 Design and Implementation Goals
This project demonstrates proficiency in:
1.  **Advanced Data Structures:** Utilizing `TreeMap` for frequency counting and `TreeSet` for high-performance stop-word filtering.
2.  **File I/O & Stream Processing:** Implementing `JFileChooser` for dynamic file selection and `PrintWriter` for exporting processed data.
3.  **Text Normalization:** Applying RegEx-based cleaning to remove non-letter characters and standardizing case for accurate frequency mapping.
4.  **GUI Integration:** Building an interactive interface that bridges complex backend processing with user-friendly displays like `JScrollPane`.

## 🛠️ Design Component Summary

The application is built on the following logic modules:

| Module | Primary Responsibility | Key Technical Focus |
| :--- | :--- | :--- |
| **Input Controller** | File Selection | `JFileChooser` for source text and stop-word dictionaries. |
| **Filter Engine** | Noise Reduction | `TreeSet<String>` for O(log n) lookup of stop words. |
| **Frequency Mapper** | Data Analysis | `TreeMap<String, Integer>` to store and sort tags by natural order. |
| **Output Handler** | Data Persistence | Writing extracted tag-frequency pairs to localized `.txt` files. |

## ⚙️ Game Logic and Rules Implementation

### 1. **The Extraction Pipeline**
* **Loading Stop Words:** The system first ingests a sorted stop-word file into a `TreeSet`. This ensures that every word scanned from the document can be instantly validated against the "noise" list.
* **Stream Cleaning:** As the source document is read, words are stripped of punctuation and forced to lowercase to ensure "Apple" and "apple" are counted as the same tag.

### 2. **Map-Based Frequency Counting**
The application implements a classic Map-Reduce style logic:
* If a word exists in the `TreeMap`, its associated value (the count) is incremented.
* If it does not exist (and is not a stop word), it is initialized in the Map with a value of 1.
* Using a `TreeMap` ensures that the final output is automatically sorted alphabetically.

### 3. **GUI and Event Handling**
* **Real-time Reporting:** The name of the file currently being processed is displayed dynamically.
* **Scrollable Results:** A `JTextArea` allows the user to browse through thousands of extracted tags without UI lag.
* **File Save Logic:** Users can export the final frequency report, facilitating further data analysis outside of the application.

## 🖼️ Visual Feedback (System States)

| Component | Visual Representation | Event |
| :--- | :--- | :--- |
| **File Display** | Dynamic File Name Label | Updates when a new Project Gutenberg file is selected. |
| **Tag List** | Key: Value (e.g., "whale: 45") | Displays the normalized tag and its total occurrence count. |
| **Stop Word Filter** | Invisible Background Logic | Automatically omits words like "the," "and," and "is." |
| **Export Action** | Save Dialog | Prompts user to name and save the final `.txt` output. |
