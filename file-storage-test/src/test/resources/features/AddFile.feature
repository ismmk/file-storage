Feature: Add file

Scenario: Upload 1 file and search it
  When I open main page
  And Put "src/test/resources/misc/ts.jpg" in file input
  And Press upload button
  And Press search button
  Then I see "ts.jpg" file