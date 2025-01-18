package com.group.libraryapp.service.book;

import com.group.libraryapp.domain.UserRepository;
import com.group.libraryapp.domain.book.Book;
import com.group.libraryapp.domain.book.BookRepository;
import com.group.libraryapp.domain.user.User;
import com.group.libraryapp.domain.user.loanHistory.UserLoanHistoryRepository;
import com.group.libraryapp.dto.book.request.BookCreateRequest;
import com.group.libraryapp.dto.book.request.BookLoanRequest;
import com.group.libraryapp.dto.book.request.BookReturnRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserLoanHistoryRepository userLoanHistoryRepository;
    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository, UserLoanHistoryRepository userLoanHistoryRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userLoanHistoryRepository = userLoanHistoryRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveBook(BookCreateRequest request) {
        bookRepository.save(new Book(request.getName()));
    }

    @Transactional
    public void loanBook(BookLoanRequest request) {
        //1. 책 정보를 가져온다.
        Book book = bookRepository.findByName(request.getBookName()).orElseThrow(IllegalArgumentException::new);

        //2. 대출기록 정보를 확인해서 대출중인지 확인
        //3. 만약에 확인했는데 대출중이라면 예외를 발생시킵니다.
        if(userLoanHistoryRepository.existsByBookNameAndIsReturn(book.getName(), false)) {
            throw new IllegalArgumentException("진작 대출되어 있는 책입니다");
        }

        //4. 유저 정보를 가져온다.
        User user = userRepository.findByName(request.getUserName()).orElseThrow(IllegalAccessError::new);
        user.loanBook(book.getName());

        //5. 유저 정보와 책 정보를 기반으로 UserLoanHistory를 저장
        //userLoanHistoryRepository.save(new UserLoanHistory(user, book.getName()));
    }

    @Transactional
    public void returnBook(BookReturnRequest request) {
        User user = userRepository.findByName(request.getUserName()).orElseThrow(IllegalAccessError::new);

        user.returnBook(request.getBookName());
        //UserLoanHistory history = userLoanHistoryRepository.findByUserIdAndBookName(user.getId(), request.getBookName()).orElseThrow(IllegalArgumentException::new);
        //history.doReturn();
        // 얘는 Transactional의 영속성 즉, 변경감지 시스템이 있기 때문에 따로 해주지 않아도 된다.
        // userLoanHistoryRepository.save(history);
    }
}
