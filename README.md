
# FetchApp 

FetchApp is an Android application built using Kotlin that fetches JSON information from a URL and displays it in a RecyclerView. It was built according to the requirements of the Fetch Rewards Coding Exercise for the Android Engineer Apprentice role. Apart from the basic requirements, I've also added some additional features which make the app more easy to use and informative.

##  Built With

- **Language**: Kotlin
- **UI**: Material Design, ConstraintLayout, RecyclerView
- **Tools**: Android Studio, Gradle
- **Data Parsing**: Gson





## Core (Basic) Features

- Retrieves the data from https://hiring.fetch.com/hiring.json.
- Display the list of items to the user based on the following requirements:
    - Display all the items grouped by "listId"
    - Sort the results first by "listId" then by "name" when displaying.
    - Filter out any items where "name" is blank or null.

## Additional Features
- A default view of the original JSON list of 1000 values before being modified according to the requirements.
- The Apply Features button converts the `defaultList` accordingly, by first filtering out the null values, then sorting with `listId` and `name`, and then grouping the data by their `listId` values.
- The Reset button takes the user back to the default view of the original JSON list.
- The Go to top button takes the user to the top of the screen, since the long list can make scrolling an unpleasant experience.
- The toolbar displays the total count of the list, and compares it to the original JSON list to show how many items were filtered out. 
- Added a toast to inform about failure in fetching data.
- Added Light and Dark mode versions.
- Added support for Landscape orientation.
- Added an app icon to replace the default icon. 
- Added a toolbar at the top, and UI improvements such as beautification of cards, to improve the app's appearance.




## Documentation

The app consists of the following:
```
app/
├── manifests/
├── kotlin+java/
│   ├── com.example.fetchapp/
│       ├── MainActivity.kt
│       ├── Request.kt
│       ├── RequestGroup.kt
│       └── RVAdapter.kt
├── res/
│   ├── drawable/
│   │   ├── ic_launcher_background.xml
│   │   ├── ic_launcher_foreground.xml
│   │   └── ic_load.xml
│   ├── layout/
│   │   ├── activity_main.xml
│   │   ├── rv_group.xml
│   │   └── rv_row.xml
│   ├── mipmap/
│   ├── values/
│   │   ├── colors.xml
│   │   ├── colors.xml (night)
│   │   ├── ic_launcher_background.xml
│   │   ├── strings.xml
│   │   ├── themes.xml
│   │   └── themes.xml (night)
│   └── xml/
├── res/ (generated)
└── Gradle Scripts/
    ├── build.gradle.kts (Project: FetchApp)
    └── build.gradle.kts (Module :app)
```

### Main Logic:

The main logic is contained in the `MainActivity.kt` file. Other than that, the `RVAdapter` is used to handle the RecyclerView Adapter logic, the `Request.kt` represents the request's JSON objects and `RequestGroup.kt` represents the request along with the grouped headers to display the modified list when Apply Filters is pressed.

`MainActivity.kt`:
This file contains the crux of the project. It handles the fetching, filtering, sorting and grouping, binding of the buttons and components, and updating the RecyclerView.

- `fetchData(): Thread` creates a Thread that connects to https://hiring.fetch.com/hiring.json, parses the response into a list using Gson, calls `updateUI()` on success and displays a toast on failure.

- `updateUI(requests: List<Request>)` sets `RVAdapter` to show the full unfiltered list. Also updates the toolbar subtitle to show the unfiltered list's count.

- `applyFilter(requests: List<Request>)` sets `RVAdapter` to show the modified list `finalList` after filtering, sorting and grouping. Grouping converts it to a map, so it had to be flattened to a list and keep the data in the form of it's grouped headers and request items. Also updates the toolbar subtitle to show the finalList's count compared to the original.

`RVAdapter.kt`: 
This file is a RecyclerView.Adapter implementation that supports two types of views- Group headers and Individual request rows.

- `class RVAdapter(private val requestList: List<RequestGroup>)` manages the view holders and binds the data.
    - `class RequestViewHolder(itemView: View)` holds references to the views for displaying the request (`idText`,`listIdText`,`nameText`).
    - `class GroupViewHolder(itemView: View)`holds the reference to the view for displaying the group header (`headerText`).

- `onCreateViewHolder()` inflates either the group header layout (rv_group.xml) or request row layout (rv_row.xml) depending on the viewType.
- `onBindViewHolder()` binds the appropriate data to the view holders.
- `getItemViewType` is used to differentiate between header and data rows.
- `getItemCount` returns the number of items to be displayed.

`Request.kt`:
This is a data holding class used to represent each JSON object.

`RequestGroup.kt`:
This interface is used to represent two types of items in a RecyclerView. It makes it easier to work with in the RecyclerView.
- `GroupItem` for the header, 
- `DataItem` for the Request object.














## Related

One thing I noticed while testing the app was that the sorting is irregular due to `name` being used to perform secondary sorting. For example, ID: 221 is smaller than ID: 29 and ID: 111 is smaller than ID: 18. This is because lexicographical sorting is being used to sort the data by name, which causes that. If `id` was used instead, this would not have been the case.

