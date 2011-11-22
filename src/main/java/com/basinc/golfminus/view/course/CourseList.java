package com.basinc.golfminus.view.course;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.logging.Logger;
import org.jboss.seam.transaction.Transactional;
import org.primefaces.model.LazyDataModel;

import com.basinc.golfminus.domain.Course;
import com.basinc.golfminus.domain.Course_;
import com.basinc.golfminus.domain.Facility;
import com.basinc.golfminus.util.PersistenceUtil;

@Transactional
@Stateful
@ConversationScoped
@Named
/**
 * Replicate the Seam2 concept where this would be a backing bean for a page that displays a List of Clubs.
 * 
 * @author Scott
 *
 */
public class CourseList extends PersistenceUtil {
	
//  @Inject
  private static Logger log = Logger.getLogger(CourseList.class);
  @Inject private LazyDataModel<Course> lazyDataModel;

  private List<Course> courses;
  
  public void find() {
      queryCourses();
  }

  private void queryCourses() {
      CriteriaBuilder builder = entityManager.getCriteriaBuilder();
      CriteriaQuery<Course> query = builder.createQuery(Course.class);
      Root<Course> root = query.from(Course.class);
      query.select(root);
      query.orderBy(builder.asc(root.get(Course_.name)));
      List<Course> results = entityManager.createQuery(query).getResultList();
      setCourses(results);
	}

	@Produces
	@Named(value="courses")
	public List<Course> getCourses() {
		return courses;
	}

	public void setCourses(List<Course> courses) {
		this.courses = courses;
	}

	public void deleteCourse(int id) {
		log.warn("Attempting to delete Course: "+id);
		Course course = entityManager.find(Course.class, id);
	    getEntityManager().joinTransaction();
	    Facility facility = course.getFacility();
	    facility.removCourse(course);
	    if (facility.getCourses().size()==0) {
	    	entityManager.remove(facility);
	    }
		entityManager.flush();
	}
	
//	@End
    public void addCourse() {
    	log.info("Add new  Course");
    }

	public LazyDataModel<Course> getLazyDataModel() {
		return lazyDataModel;
	}

	public void setLazyDataModel(LazyDataModel<Course> lazyDataModel) {
		this.lazyDataModel = lazyDataModel;
	}

}
