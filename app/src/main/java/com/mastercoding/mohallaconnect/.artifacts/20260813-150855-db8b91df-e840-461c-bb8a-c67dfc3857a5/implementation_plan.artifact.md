# Posts Feature Implementation Plan

This plan outlines the steps to implement a fully functional, real-time posting system for the Mohalla Connect app using MVVM and Firebase.

## Overview of the Implementation

The goal is to allow users to create posts with text, images, and neighborhood tags, which will then be displayed in a real-time feed filtered by the user's location.

### 1. Data Layer (Model & Repository)
*   **Post Model**: Define a `Post` data class to represent the structure of a post in Firestore.
*   **Post Repository**: A central class to handle Firestore queries (uploading, fetching, voting) and Firebase Storage (uploading post images).

### 2. ViewModel Layer
*   **FeedViewModel**: This will be the bridge between the UI and the Data. It will:
    *   Expose a real-time stream of posts to the `FeedScreen`.
    *   Handle the logic for creating a new post (including image upload).
    *   Manage upvote/downvote operations.

### 3. UI Layer Integration
*   **CreatePost Integration**: Connect the `CreatePostsPage` UI to the ViewModel to save real data.
*   **Feed Integration**: Update `FeedScreen` to observe the ViewModel and display real posts from Firestore.
*   **Interactivity**: Make the upvote, downvote, and comment buttons functional.

---

## Step-by-Step Execution

### Step 1: Define the Post Data Model
We need a standard structure for posts that includes author details, content, location, and timestamps.

### Step 2: Create the Post Repository
Implement the "heavy lifting" code to talk to Firebase. This includes real-time listeners so the feed updates automatically when anyone posts.

### Step 3: Implement FeedViewModel
Set up the state management for the feed and the creation process.

### Step 4: Hook up the "Post" button
Make the "Post" button in `CreatePostsPage` actually upload the data to Firestore.

### Step 5: Finalize the Feed
Replace the sample data in `FeedScreen` with the real-time data from the ViewModel.

---

## Verification Plan

### Manual Verification
*   Create a post with text and verify it appears in Firestore.
*   Create a post with an image and verify the image is stored in Firebase Storage and linked correctly.
*   Verify that only posts from a specific neighborhood (or all) are shown based on logic.
*   Test upvote/downvote toggling.
