package com.example.library.service;

import com.example.library.entity.Post;
import com.example.library.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    public List<Post> getTop5MostLikedPosts() {
        return postRepository.findTop5ByOrderByLikesDesc();
    }

    public Post savePost(Post post) {
        return postRepository.save(post);
    }

    public Post getPostById(int postId) {
        return postRepository.findById(postId).orElse(null);
    }

    public List<Post> getAllPosts() { return postRepository.findAll();
    }

    public void deletePost(int id) { postRepository.deleteById(id);
    }
}