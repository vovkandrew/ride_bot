package org.project.repository;

import org.project.model.City;
import org.project.model.Country;
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

import java.util.List;
import java.util.Optional;
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

    @Query("select r.telegramUserId from Route r where r.status = ?1 and r.countryFrom = ?2 and r.cityFrom = ?3 and " +
                  "r.countryTo = ?4 and r.cityTo = ?5 and r.userType = ?6")
    List<Long> getAllTelegramUsersByStatusAndRouteInfoAndUserType(Status status, Country countryFrom, City cityFrom,
                                                                  Country countryTo, City cityTo, UserType userType);

    @Query ("select r from Route r where r.telegramUserId = ?1 and r.countryFrom = ?2 and r.cityFrom = ?3 and " +
                    "r.countryTo = ?4 and r.cityTo = ?5 and r.status = ?6 and r.userType = ?7")
    Optional<Route> findRouteByDetails(long telegramUserId, Country countryFrom, City cityFrom, Country countryTo,
                                       City cityTo, Status status, UserType userType);

	@Transactional
	@Modifying
	@Query ("update Route r set r.picked = false where r.telegramUserId = ?1 and r.picked = true")
    void updateToFalsePickedByTelegramUserId(long telegramUserId);

    @Query ("select r from Route r where r.telegramUserId = ?1 and r.picked = ?2 and r.userType = ?3")
    Route getByTelegramUserIdAndPickedAndUserType(long telegramUserId, boolean picked, UserType userType);

    @Query ("select (count(r) > 0) from Route r " +
            "where r.countryFrom = ?1 and r.cityFrom = ?2 and r.countryTo = ?3 and r.cityTo = ?4 and r.userType = ?5")
    boolean existsByRouteDetailsAndUserType(Country countryFrom, City cityFrom,
                                            Country countryTo, City cityTo, UserType userType);
}
