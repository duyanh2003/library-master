package com.example.library.service;

import com.example.library.entity.ReadingList;
import com.example.library.entity.User;
import com.example.library.repository.ReadingListRepository;
import com.example.library.service.inter.ReadingListService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReadingListServiceImpl implements ReadingListService {

    private final ReadingListRepository readingListRepository;

    public ReadingListServiceImpl(ReadingListRepository readingListRepository) {
        this.readingListRepository = readingListRepository;
    }

    @Override
    public List<ReadingList> findReadingListByUser(User user) {
        return readingListRepository.findReadingListByUser(user);
    }

    @Override
    public ReadingList findReadingListByUserIdAndBookId(int userId, int bookId) {
        return readingListRepository.findReadingListByUser_IdAndBook_Id(userId, bookId);
    }

    @Override
    public ReadingList save(ReadingList readingList) {
        return readingListRepository.save(readingList);
    }

    @Transactional
    @Override
    public void deleteReadingListByUserIdAndBookId(int userId, int bookId) {
        readingListRepository.deleteReadingListByUser_IdAndBook_Id(userId, bookId);
    }

}
