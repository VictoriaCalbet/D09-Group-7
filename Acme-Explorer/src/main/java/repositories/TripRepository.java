
package repositories;

import java.util.Collection;
import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import domain.Trip;

@Repository
public interface TripRepository extends JpaRepository<Trip, Integer> {

	//Find Trip by audit
	@Query("select t from Trip t join t.audits a where a.id=?1")
	Trip findTripByAudit(int idAudit);
	//Needed for sponsorships 
	@Query("select t from Trip t join t.sponsorships s where s.id= ?1")
	Trip findTripBySponsorship(int sponsorshipId);

	@Query("select t from Trip t where t.publicationDate < CURRENT_TIMESTAMP")
	Collection<Trip> findTripPublished();

	@Query("select t from Trip t where t.publicationDate > CURRENT_TIMESTAMP")
	Collection<Trip> findTripsNotPublished();

	@Query("select t from Trip t where t.publicationDate > CURRENT_TIMESTAMP AND t.manager.id = ?1")
	Collection<Trip> findAllNotPublishedByManagerId(int managerId);

	@Query("select t from Trip t where t.publicationDate < CURRENT_TIMESTAMP AND t.startMoment > CURRENT_TIMESTAMP")
	Collection<Trip> findAllPublishedAndNotStarted();

	@Query("select t from Trip t where t.publicationDate < CURRENT_TIMESTAMP AND t.startMoment > CURRENT_TIMESTAMP AND t.cancelled IS FALSE")
	Collection<Trip> findAllPublishedNotStartedAndNotCancelled();

	//Find trips not ended
	@Query("select t from Trip t where t.endMoment > CURRENT_TIMESTAMP")
	Collection<Trip> findTripsNotEnded();

	@Query("select a.trip from Application a, Explorer e where e.id = ?1 and a in elements(e.applications) " + "and a.trip.startMoment <= ?2 and a.trip.startMoment >= CURRENT_TIMESTAMP")
	Collection<Trip> findTripsExplorerInLessThanAMonth(int idExplorer, Date nowPlusAMonth);

	@Query("select a.trip from Application a, Explorer e where e.id = ?1 and a in elements(e.applications) " + "and a.trip.startMoment <= CURRENT_TIMESTAMP")
	Collection<Trip> findTripsExplorerStarted(int idExplorer);

	@Query("select distinct t from Trip t, Application a, Explorer e where e.id=?1 and a.trip.id=t.id and a not in elements(e.applications) " + "and t.publicationDate < CURRENT_TIMESTAMP AND t.startMoment > CURRENT_TIMESTAMP")
	Collection<Trip> findTripsWithNotPrincipalApply(int explorerId);

	//C- 10.3 Search for trips using a single key word that must be contained either in their tick-ers, titles, or descriptions
	@Query("select t from Trip t where (t.ticker like %?1% or t.title like %?1% or t.description like %?1%) and t.publicationDate < CURRENT_TIMESTAMP")
	Collection<Trip> findTripByKeyWord(String keyWord);

	//12.1 Find a trip by a ticker
	@Query("select t from Trip t where t.ticker = ?1")
	Trip findTripByTicker(String ticker);

	// Search a Trip by its parameters
	@Query("select t from Trip t where (?1=null or t.price >= ?1) and (?2=null or t.price <= ?2)" + "and (?3=null " + "or year(t.startMoment) > year(?3) " + "or (month(t.startMoment) > month(?3) and year(t.startMoment) = year(?3)) "
		+ "or (month(t.startMoment) < month(?3) and year(t.startMoment) > year(?3)) " + "or (day(t.startMoment) >= day(?3) and month(t.startMoment) >= month(?3) and year(t.startMoment) >= year(?3)) "
		+ "or (day(t.startMoment) < day(?3) and month(t.startMoment) > month(?3) and year(t.startMoment) >= year(?3))) " + "and (?4=null " + "or year(t.endMoment) < year(?4) " + "or (month(t.endMoment) < month(?4) and year(t.endMoment) = year(?4)) "
		+ "or (month(t.endMoment) > month(?4) and year(t.endMoment) < year(?4)) " + "or (day(t.endMoment) <= day(?4) and month(t.endMoment) <= month(?4) and year(t.endMoment) <= year(?4)) "
		+ "or (day(t.endMoment) > day(?4) and month(t.endMoment) < month(?4) and year(t.endMoment) <= year(?4))) " + "and (?5 = '' or ?5=null or t.title like %?5% or t.description like %?5% or t.ticker like %?5%)")
	Collection<Trip> search(Double minPrice, Double maxPrice, Date startDate, Date endDate, String keyword);
	//Search 2
	@Query("select t from Trip t where" + "((?1=null or t.price >= ?1) and (?2=null or t.price <= ?2)" + "and (" + "?3=null " + "or year(t.startMoment) > year(?3) or (month(t.startMoment) > month(?3) and year(t.startMoment) = year(?3))"
		+ "or (month(t.startMoment) = month(?3) and year(t.startMoment) = year(?3) and day(t.startMoment) >= day(?3))" + ")" + "and (" + "?4=null or year(t.endMoment) < year(?4) " + "or (month(t.endMoment) < month(?4) and year(t.endMoment) = year(?4)) "
		+ "or (month(t.endMoment) = month(?4) and year(t.endMoment) = year(?4) and day(t.endMoment) <= day(?4))" + ")" + "and (?5 = '' or ?5=null or t.title like %?5% or t.description like %?5%))")
	Collection<Trip> search2(Double minPrice, Double maxPrice, Date startDate, Date endDate, String keyword);
	// TODO LIMIT NO EXISTE EN HQL.
	//	@Query("select f.found from Finder f, SystemConfiguration s where f = ?1 limit s.defaultFinderNumber")
	//	Collection<Trip> getLimitFound(Finder finder);

	//Sum the prices of the stages of a trip
	@Query("select sum(s.price) from Trip t join t.stages s where t.id=?1")
	Double sumByPriceStage(int idTrip);

	//C-14.6.3.1 The average price of the trips
	@Query("select avg(t.price) from Trip t")
	Double findAveragePriceOfTrips();

	//C-14.6.3.2 The minimum price of the trips
	@Query("select min(t.price) from Trip t")
	Double findMinPriceOfTrips();

	//C-14.6.3.3 The maximum price of the trips
	@Query("select max(t.price) from Trip t")
	Double findMaxPriceOfTrips();

	//C-14.6.3.4 The standard deviation of the price of the trips
	@Query("select sqrt(sum(t.price * t.price) / count(t.price) - (avg(t.price) * avg(t.price))) from Trip t")
	Double findStandardDeviationOfPriceOfTrips();

	//C-14.6.9 The ratio of trips that have been cancelled versus the total number of trips that have been organized
	@Query("select count(t)*1.0/(select count(t1) from Trip t1) from Trip t where t.cancelled = true")
	Double findRatioOfTripsVersusNumberOfTrips();

	//C-14.6.10 The listing of trips that have got at least 10% more applications than the average, ordered by number of applications
	@Query("select t from Trip t where t.applications.size > (select avg(t.applications.size)*1.1 from Trip t)")
	Collection<Trip> findTripsWithAverageHigherThan10PerCentOrderedByApplicationNumber();

	//B-16.4.3 The ratio of trips with an audit record
	@Query("select sum(case when t.audits.size=1 then 1 else 0 end)*1.0/count(t) from Trip t")
	Double findRatioOfTripsWithAuditRecord();

	@Query("select a.trip from Application a, Explorer e where e.id = ?1 and a in elements(e.applications) and a.trip.endMoment < CURRENT_TIMESTAMP and a.status='ACCEPTED'")
	Collection<Trip> findEndedTripsWithAcceptedApplication(final int explorerId);

}
