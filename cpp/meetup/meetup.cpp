#include "meetup.h"

namespace meetup {

scheduler::scheduler(boost::gregorian::greg_month month, int year)
    : month_(month), year_(year) {}

// Helper method to find a "teenth" day (13th-19th) that falls on a specific weekday
boost::gregorian::date scheduler::find_teenth(boost::gregorian::greg_weekday weekday) const {
    // Start from the 13th of the month
    boost::gregorian::date start_date(year_, month_, 13);
    
    // Calculate days to add to get to the desired weekday
    int days_to_add = (weekday.as_number() - start_date.day_of_week().as_number() + 7) % 7;
    
    // Return the date of the desired weekday
    return start_date + boost::gregorian::days(days_to_add);
}

// Helper method to find the nth occurrence of a weekday in the month
boost::gregorian::date scheduler::find_nth_weekday(int n, boost::gregorian::greg_weekday weekday) const {
    // Start from the first day of the month
    boost::gregorian::date start_date(year_, month_, 1);
    
    // Calculate days to add to get to the first occurrence of the desired weekday
    int days_to_first = (weekday.as_number() - start_date.day_of_week().as_number() + 7) % 7;
    
    // Calculate the date of the nth occurrence
    return start_date + boost::gregorian::days(days_to_first + (n - 1) * 7);
}

// Helper method to find the last occurrence of a weekday in the month
boost::gregorian::date scheduler::find_last_weekday(boost::gregorian::greg_weekday weekday) const {
    // Get the last day of the month
    boost::gregorian::date end_of_month = boost::gregorian::date(year_, month_, 1).end_of_month();
    
    // Calculate days to subtract to get to the last occurrence of the desired weekday
    int days_to_subtract = (end_of_month.day_of_week().as_number() - weekday.as_number() + 7) % 7;
    
    // Return the date of the last occurrence of the desired weekday
    return end_of_month - boost::gregorian::days(days_to_subtract);
}

// Teenth methods
boost::gregorian::date scheduler::monteenth() const {
    return find_teenth(boost::gregorian::Monday);
}

boost::gregorian::date scheduler::tuesteenth() const {
    return find_teenth(boost::gregorian::Tuesday);
}

boost::gregorian::date scheduler::wednesteenth() const {
    return find_teenth(boost::gregorian::Wednesday);
}

boost::gregorian::date scheduler::thursteenth() const {
    return find_teenth(boost::gregorian::Thursday);
}

boost::gregorian::date scheduler::friteenth() const {
    return find_teenth(boost::gregorian::Friday);
}

boost::gregorian::date scheduler::saturteenth() const {
    return find_teenth(boost::gregorian::Saturday);
}

boost::gregorian::date scheduler::sunteenth() const {
    return find_teenth(boost::gregorian::Sunday);
}

// First weekday methods
boost::gregorian::date scheduler::first_monday() const {
    return find_nth_weekday(1, boost::gregorian::Monday);
}

boost::gregorian::date scheduler::first_tuesday() const {
    return find_nth_weekday(1, boost::gregorian::Tuesday);
}

boost::gregorian::date scheduler::first_wednesday() const {
    return find_nth_weekday(1, boost::gregorian::Wednesday);
}

boost::gregorian::date scheduler::first_thursday() const {
    return find_nth_weekday(1, boost::gregorian::Thursday);
}

boost::gregorian::date scheduler::first_friday() const {
    return find_nth_weekday(1, boost::gregorian::Friday);
}

boost::gregorian::date scheduler::first_saturday() const {
    return find_nth_weekday(1, boost::gregorian::Saturday);
}

boost::gregorian::date scheduler::first_sunday() const {
    return find_nth_weekday(1, boost::gregorian::Sunday);
}

// Second weekday methods
boost::gregorian::date scheduler::second_monday() const {
    return find_nth_weekday(2, boost::gregorian::Monday);
}

boost::gregorian::date scheduler::second_tuesday() const {
    return find_nth_weekday(2, boost::gregorian::Tuesday);
}

boost::gregorian::date scheduler::second_wednesday() const {
    return find_nth_weekday(2, boost::gregorian::Wednesday);
}

boost::gregorian::date scheduler::second_thursday() const {
    return find_nth_weekday(2, boost::gregorian::Thursday);
}

boost::gregorian::date scheduler::second_friday() const {
    return find_nth_weekday(2, boost::gregorian::Friday);
}

boost::gregorian::date scheduler::second_saturday() const {
    return find_nth_weekday(2, boost::gregorian::Saturday);
}

boost::gregorian::date scheduler::second_sunday() const {
    return find_nth_weekday(2, boost::gregorian::Sunday);
}

// Third weekday methods
boost::gregorian::date scheduler::third_monday() const {
    return find_nth_weekday(3, boost::gregorian::Monday);
}

boost::gregorian::date scheduler::third_tuesday() const {
    return find_nth_weekday(3, boost::gregorian::Tuesday);
}

boost::gregorian::date scheduler::third_wednesday() const {
    return find_nth_weekday(3, boost::gregorian::Wednesday);
}

boost::gregorian::date scheduler::third_thursday() const {
    return find_nth_weekday(3, boost::gregorian::Thursday);
}

boost::gregorian::date scheduler::third_friday() const {
    return find_nth_weekday(3, boost::gregorian::Friday);
}

boost::gregorian::date scheduler::third_saturday() const {
    return find_nth_weekday(3, boost::gregorian::Saturday);
}

boost::gregorian::date scheduler::third_sunday() const {
    return find_nth_weekday(3, boost::gregorian::Sunday);
}

// Fourth weekday methods
boost::gregorian::date scheduler::fourth_monday() const {
    return find_nth_weekday(4, boost::gregorian::Monday);
}

boost::gregorian::date scheduler::fourth_tuesday() const {
    return find_nth_weekday(4, boost::gregorian::Tuesday);
}

boost::gregorian::date scheduler::fourth_wednesday() const {
    return find_nth_weekday(4, boost::gregorian::Wednesday);
}

boost::gregorian::date scheduler::fourth_thursday() const {
    return find_nth_weekday(4, boost::gregorian::Thursday);
}

boost::gregorian::date scheduler::fourth_friday() const {
    return find_nth_weekday(4, boost::gregorian::Friday);
}

boost::gregorian::date scheduler::fourth_saturday() const {
    return find_nth_weekday(4, boost::gregorian::Saturday);
}

boost::gregorian::date scheduler::fourth_sunday() const {
    return find_nth_weekday(4, boost::gregorian::Sunday);
}

// Last weekday methods
boost::gregorian::date scheduler::last_monday() const {
    return find_last_weekday(boost::gregorian::Monday);
}

boost::gregorian::date scheduler::last_tuesday() const {
    return find_last_weekday(boost::gregorian::Tuesday);
}

boost::gregorian::date scheduler::last_wednesday() const {
    return find_last_weekday(boost::gregorian::Wednesday);
}

boost::gregorian::date scheduler::last_thursday() const {
    return find_last_weekday(boost::gregorian::Thursday);
}

boost::gregorian::date scheduler::last_friday() const {
    return find_last_weekday(boost::gregorian::Friday);
}

boost::gregorian::date scheduler::last_saturday() const {
    return find_last_weekday(boost::gregorian::Saturday);
}

boost::gregorian::date scheduler::last_sunday() const {
    return find_last_weekday(boost::gregorian::Sunday);
}

}  // namespace meetup
