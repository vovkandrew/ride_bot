package org.project.repository;

import org.project.model.Route;
import org.project.util.enums.Status;
import org.project.util.enums.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Repository
public interface RouteRepository extends JpaRepository<Route, Long> {
    @Transactional
    @Modifying
    @Query("delete from Route r where r.telegramUserId = ?1 and r.status = ?2")
    void deleteAllByTelegramUserIdAndStatus(long telegramUserId, Status status);

    @Transactional
    @Modifying
    @Query("delete from Route r where r.telegramUserId = ?1 and r.status = ?2 and r.userType = ?3")
    void deleteAllByTelegramUserIdAndStatusAndAndUserType(long telegramUserId, Status status, UserType userType);

    @Query("select r from Route r where r.telegramUserId = ?1 and r.status = ?2")
    Route getRouteByTelegramUserIdAndStatus(long telegramUserId, Status status);

    @Query("select r from Route r where r.telegramUserId = ?1 and r.status = ?2")
    Page<Route> findAllByTelegramUserIdAndStatus(long telegramUserId, Status status, Pageable pageable);

    @Query("select r from Route r where r.telegramUserId = ?1 and r.status = ?2 and r.userType = ?3")
    Page<Route> findAllByTelegramUserIdAndStatusAndUserType(long telegramUserId, Status status, UserType userType,
                                                            Pageable pageable);

    @Query("select r from Route r where r.telegramUserId = ?1 and r.status = ?2")
    Set<Route> getAllByTelegramUserIdAndStatus(long telegramUserId, Status status);

    @Query("select r from Route r where r.telegramUserId = ?1 and r.status = ?2 and r.userType = ?3")
    Route getRouteByTelegramUserIdAndStatusAndUserType(long telegramUserId, Status status, UserType userType);
}
