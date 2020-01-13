package com.project.cookbook.repository;

import com.project.cookbook.model.Book;
import com.project.cookbook.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findBookByUserEquals(User user);
}
