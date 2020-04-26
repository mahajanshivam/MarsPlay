# MarsPlay
Image upload sample app for Marsplay assignment

1. The Main Activity of the app contains a grid of photos showing all the pictures that have been
    uploaded on the Firebase Storage

2. A Floating button on the bottom right initiates the upload photo flow by providing 2 options
    to upload pics  - Camera and Gallery

3. Camera view also has basic camera features like flash on off, zoom in out, tap to focus.

4. Images are uploaded on the Firebase storage using FireBase storage SDK.

5. MVVM architecture is used with classes like MainViewModel and MainRepository.

6. Other jetpack components are used like LiveData and Databinding in activity, Fragment and adapter

7. Dependency Injection has been implemented using Dagger2.

8. When the grid item is clicked on the main page grid, PhotoActivity is oepned up showing the
    particular image in expanded view which can be Zoomed in and out.

9. All the pages in the app support both Portrait and landscape orientation.

10. GridLayoutManager is used for Main Activity grid recyclerview and also uses item Decoration for
    proper spacing.

11. Bottom sheet Dialog Fragment is also used to show the option for Gallery or Camera.

