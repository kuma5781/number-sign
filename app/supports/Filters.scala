package supports

import javax.inject.Inject
import play.api.http.DefaultHttpFilters
import play.filters.cors.CORSFilter

/**
  * Httpフィルタ
  */
class Filters @Inject()(corsFilter: CORSFilter) extends DefaultHttpFilters(corsFilter)
