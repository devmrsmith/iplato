# iPlato SpaceX technical exercise

### Specification:
Implement SpaceX ship list informative application:

As a user, I want to see the list of ships, when I open the application.
•	For each row these attributes should be displayed: name of the ship, year built, homeport and ship image.

•	When the application is opened for the first time it should display a welcome screen.

•	As a user, I want to have possibility to filter the list and see only active ships.

The application should display a loading indicator while fetching data.
Also, app should allow user to refresh the data.

API Details
Documentation:
https://docs.spacexdata.com/?version=latest
To retrieve all the ship list (Set the offset and limits to show 10 records everytime)
https://api.spacexdata.com/v3/ships

Rules
Your code must be written in Kotlin and should use the most modern/recent architecture and design patterns & components.
Your code must be unit tested.


### Implementation:
- Kotlin
- MVVM / Repository / LiveData / Databinding pattern
- Coroutines
- Dagger for Dependency Injection
- Retrofit for API calls
- Testing uses a MockWebServer to simulate API calls - returning json from locally stored files
