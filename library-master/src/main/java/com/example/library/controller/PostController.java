
package com.example.library.controller;

import com.example.library.dto.PostDto;
import com.example.library.entity.Comment;
import com.example.library.entity.Like;
import com.example.library.entity.Post;
import com.example.library.entity.User;
import com.example.library.service.CommentService;
import com.example.library.service.LikeService;
import com.example.library.service.PostService;
import com.example.library.service.inter.UserService;
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

    @Autowired
    private UserService userService;

    @GetMapping
    public String getAllPosts(Model model) {
        List<Post> posts = postService.getAllPosts();
        List<Post> topPosts = postService.getTop5MostLikedPosts();
        model.addAttribute("posts", posts);
        model.addAttribute("topPosts", topPosts);
        return "posts";
    }

    @GetMapping("/create")
    public String getCreatePostPage(Model model) {
        model.addAttribute("postDto", new PostDto());
        return "posts-create";
    }

    @PostMapping
    public String createPost(@ModelAttribute PostDto postDto, Principal principal) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        post.setSurname(principal.getName());
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
        if (!post.getSurname().equals(principal.getName())) {
            return "redirect:/posts";
        }
        model.addAttribute("post", post);
        return "posts-edit";
    }

    @PostMapping("/{id}/edit")
    public String editPost(@PathVariable int id, @ModelAttribute Post post, Principal principal) {
        Post existingPost = postService.getPostById(id);
        if (!existingPost.getSurname().equals(principal.getName())) {
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
        if (post.getSurname().equals(principal.getName())) {
            postService.deletePost(id);
        }
        return "redirect:/posts";
    }

    @PostMapping("/{postId}/comments")
    public String addComment(@PathVariable int postId, @RequestParam(name = "content") String content) {
        Comment comment = new Comment();
        comment.setContent(content);
        Post post = postService.getPostById(postId);
        comment.setPost(post);
        commentService.saveComment(comment);
        return "redirect:/posts/" + postId;
    }

    @PostMapping("/{postId}/likes")
    public String addLike(@PathVariable int postId, Principal principal) {
        Post post = postService.getPostById(postId);
        if (principal.getName() == null || principal.getName().equals(post.getSurname())) {
            return "redirect:/posts/" + postId;
        }
        User user = userService.findUserBySurname(principal.getName());
        Like like = new Like();
        like.setUser(user);
        like.setPost(post);
        likeService.saveLike(like);
        return "redirect:/posts/" + postId;
    }
}