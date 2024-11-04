package com.sixa.quizservice.service;

import com.sixa.quizservice.dao.QuizDao;
import com.sixa.quizservice.model.Question;
import com.sixa.quizservice.model.QuestionWrapper;
import com.sixa.quizservice.model.Quiz;
import com.sixa.quizservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

//    @Autowired
//    private QuestionDao questionDao;

//    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
//        try {
//        List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);
//        Quiz quiz = new Quiz();
//        quiz.setTitle(title);
//        quiz.setQuestions(questions);
//        quizDao.save(quiz);
//            return new ResponseEntity<>("success", HttpStatus.OK);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//            return new ResponseEntity<>("error", HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        try {
            Optional<Quiz> quiz = quizDao.findById(id);
            List<Question> questionsFromDB = quiz.get().getQuestions();
            List<QuestionWrapper> questionsForUsers = new ArrayList<>();

            for (Question q : questionsFromDB) {
                QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
                questionsForUsers.add(qw);
            }
            return new ResponseEntity<>(questionsForUsers, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        try {
            Quiz quiz = quizDao.findById(id).get();
            List<Question> questions = quiz.getQuestions();
            int right = 0;
            int i = 0;
            for (Response response : responses) {
                if (response.getResponse().equals(questions.get(i).getRightAnswer()))
                    right++;
                i++;
            }
            return new ResponseEntity<>(right, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
