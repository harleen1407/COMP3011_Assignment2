@echo off
cd /d "%~dp0"

echo =========================================
echo COMP3011 Assignment 2 Performance Tests
echo =========================================

echo.
echo TEST 1: No effects
mvn javafx:run "-Djavafx.args=simpsons.mp4"

echo.
echo TEST 2: One effect
mvn javafx:run "-Djavafx.args=-w simpsons.mp4"

echo.
echo TEST 3: Four effects
mvn javafx:run "-Djavafx.args=-nssw simpsons.mp4"

echo.
echo TEST 4: All effects
mvn javafx:run "-Djavafx.args=-nsfwyvdjmbp simpsons.mp4"

echo.
echo All test runs completed.
pause