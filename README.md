# Fake Gold Bar Finder

## Overview
The Fake Gold Bar Finder is a Java application that uses Selenium WebDriver to automate the process of finding a fake gold bar among 9 bars on the website https://sdetchallenge.fetch.com/. The application simulates weighing operations to identify which bar has a different weight (either heavier or lighter) compared to the others.

## Features
- Automates browser interactions using Selenium WebDriver
- Implements a logical algorithm to identify the fake gold bar in minimal weighing operations
- Uses WebDriverManager for easy setup of ChromeDriver
- Provides console output for the final step of the weighing process when the fake bar is identified

## Prerequisites
- Java JDK 8 or higher
- Maven (for dependency management)

## Setup
1. Clone this repository to your local machine
2. Ensure you have Java and Maven installed and properly configured.
3. Open the project in your preferred Java IDE, recommended IntelliJ IDEA

## Dependencies
The project uses the following main dependencies:
- Selenium WebDriver
- WebDriverManager
- ChromeDriver

These dependencies are managed through Maven and will be automatically downloaded when you build the project.

## Running the Application
1. Open the project in your IDE.
2. Run the `main` method in the `FakeGoldBarFinder` class.
3. The application will open a Chrome browser, navigate to the challenge website, and perform the weighing operations.
4. The fake gold bar will be identified and printed in the console output.

## How it Works
1. The application starts by comparing bars [0, 1, 2] against [3, 4, 5].
2. Based on the result, it narrows down which group contains the fake bar.
3. It then performs additional weighing operations within the identified group to pinpoint the exact fake bar.
4. The process is designed to find the fake bar in a minimal number of weighs.

## Output
The application will print the weighing results and the final identification of the fake bar to the console. 

## Troubleshooting
- If you encounter any issues with ChromeDriver, ensure that your Chrome browser is up-to date.
- The application uses explicit waits to handle timing issues, but you may need to adjust wait times if you experience timeouts.
