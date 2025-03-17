#include "gigasecond.h"
#include "boost/date_time/posix_time/posix_time.hpp"

namespace gigasecond {
    boost::posix_time::ptime advance(boost::posix_time::ptime time) {
        // A gigasecond is 10^9 seconds
        return time + boost::posix_time::seconds(1000000000);
    }
}  // namespace gigasecond
