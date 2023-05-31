package unsw.piazza;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A thread in the Piazza forum.
 */
public class Thread {
    private String title;
    private List<String> tags;
    private List<String> posts;

    /**
     * Creates a new thread with a title and an initial first post.
     * @param title
     * @param firstPost
     */
    public Thread(String title, String firstPost) {
        this.title = title;
        this.tags = new ArrayList<String>();
        this.posts = new ArrayList<String>();
        this.posts.add(firstPost);
    }

    /**
     * @return The title of the thread
     */
    public String getTitle() {
        return title;
    }

    /**
     * @return A SORTED list of tags
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * @return A list of posts in this thread, in the order that they were published
     */
    public List<String> getPosts() {
        return posts;
    }

    /**
     * Adds the given post object into the list of posts in the thread.
     * @param post
     */
    public void publishPost(String post) {
        posts.add(post);
    }

    /**
     * Allows the given user to replace the thread tags (list of strings)
     * @param tags
     */
    public void setTags(String[] tags) {
        this.tags = new ArrayList<>(Arrays.asList(tags));
        this.tags.sort(null);
    }
}
