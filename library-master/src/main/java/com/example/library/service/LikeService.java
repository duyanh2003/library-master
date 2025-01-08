package com.example.library.service;

import com.example.library.entity.Like;
import com.example.library.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    public Like saveLike(Like like) {
        return likeRepository.save(like);
    }
}