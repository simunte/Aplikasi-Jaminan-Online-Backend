package com.ebizcipta.ajo.api.util;

import com.ebizcipta.ajo.api.domain.User;
import com.ebizcipta.ajo.api.domain.UserHistory;
import com.ebizcipta.ajo.api.repositories.UserHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUtil {

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    public Boolean userHistory(User userModified , String user , String action, String note){
        UserHistory userHistory = new UserHistory();
        userHistory.setAction(action);
        userHistory.setNote(note);
        userHistory.setUsersInAction(user);
        userHistory.setUser(userModified);
        userHistoryRepository.save(userHistory);
        return Boolean.TRUE;
    }

}
