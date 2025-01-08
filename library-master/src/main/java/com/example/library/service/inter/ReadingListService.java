package com.example.library.service.inter;

import com.example.library.entity.ReadingList;
import com.example.library.entity.User;

import java.util.List;

public interface ReadingListService {

    List<ReadingList> findReadingListByUser(User user);

    ReadingList findReadingListByUserIdAndBookId(int userId, int bookId);

    ReadingList save(ReadingList readingList);

    void deleteReadingListByUserIdAndBookId(int userId, int bookId);

}
