package com.example.library.controller;

import com.example.library.entity.Comment;
import com.example.library.entity.Like;
import com.example.library.entity.Post;
import com.example.library.service.CommentService;
import com.example.library.service.LikeService;
import com.example.library.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/posts")
public class PostController {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @GetMapping("/posts")
    public String getAllPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        List<Post> topPosts = postService.getTop5MostLikedPosts();
        model.addAttribute("posts", posts);
        model.addAttribute("topPosts", topPosts);
        return "posts";
    }

    @GetMapping("/create")
    public String getCreatePostPage() {
        return "posts-create";
    }

    @PostMapping
    public String createPost(@ModelAttribute Post post, Principal principal) {
        post.setUsername(principal.getName());
        postService.savePost(post);
        return "redirect:/posts";
    }

    @GetMapping("/{id}")
    public String getPostDetailsPage(@PathVariable int id, Model model) {
        Post post = postService.getPostById(id);
        model.addAttribute("post", post);
        return "posts-details";
    }

    @GetMapping("/{id}/edit")
    public String getEditPostPage(@PathVariable int id, Model model, Principal principal) {
        Post post = postService.getPostById(id);
        if (!post.getUsername().equals(principal.getName())) {
            return "redirect:/posts";
        }
        model.addAttribute("post", post);
        return "posts-edit";
    }

    @PostMapping("/{id}/edit")
    public String editPost(@PathVariable int id, @ModelAttribute Post post, Principal principal) {
        Post existingPost = postService.getPostById(id);
        if (!existingPost.getUsername().equals(principal.getName())) {
            return "redirect:/posts";
        }
        existingPost.setTitle(post.getTitle());
        existingPost.setContent(post.getContent());
        postService.savePost(existingPost);
        return "redirect:/posts/" + id;
    }

    @PostMapping("/{id}/delete")
    public String deletePost(@PathVariable int id, Principal principal) {
        Post post = postService.getPostById(id);
        if (post.getUsername().equals(principal.getName())) {
            postService.deletePost(id);
        }
        return "redirect:/posts";
    }

    @PostMapping("/{postId}/comments")
    public String addComment(@PathVariable int postId, @ModelAttribute Comment comment) {
        Post post = postService.getPostById(postId);
        comment.setPost(post);
        commentService.saveComment(comment);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/likes")
    public String addLike(@PathVariable int postId) {
        Post post = postService.getPostById(postId);
        Like like = new Like();
        like.setPost(post);
        likeService.saveLike(like);
        return "redirect:/posts/" + postId;
    }
}