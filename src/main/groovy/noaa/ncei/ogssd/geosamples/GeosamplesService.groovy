package noaa.ncei.ogssd.geosamples

import org.springframework.stereotype.Service

import java.text.SimpleDateFormat

@Service
class GeosamplesService {

/**
 * helper method to build SQL where clause and associated query values list.
 * WARNING: this method has an implicit coupling between the parameter names
 * and the database columns
 */
    static List buildWhereClause(parameters, List<String> defaultCriteria = []) {
        println("inside buildWhereClause with ${parameters} and ${defaultCriteria}")
        List criteria = new ArrayList(defaultCriteria)
        List criteriaValues = []
        if (parameters['repository']) {
            criteria << 'facility_code = ?'
            criteriaValues << parameters['repository']
        }
        if (parameters['bbox']) {
            def coords = parameters['bbox'].split(',').collect { Double.parseDouble(it.trim()) }
            criteria << "lon >= ? and lat >= ? and lon <= ? and lat <= ?"
            criteriaValues += coords
        }
        if (parameters['platform']) {
            criteria << 'platform = ?'
            criteriaValues << parameters['platform']
        }
        if (parameters['lake']) {
            criteria << 'lake = ?'
            criteriaValues << parameters['lake']
        }
        if (parameters['cruise']) {
            criteria << 'cruise = ?'
            criteriaValues << parameters['cruise']
        }
        if (parameters['device']) {
            criteria << 'device = ?'
            criteriaValues << parameters['device']
        }
        if (parameters['startDate']) {
            def startDate = new SimpleDateFormat("yyyy-MM-dd").parse(parameters['startDate'])
            criteria << "begin_date >= ?"
            criteriaValues << new java.sql.Date(startDate.getTime())
        }
        if (parameters['minDepth']) {
            criteria << 'water_depth >= ?'
            criteriaValues << parameters['minDepth']
        }
        if (parameters['maxDepth']) {
            criteria.push('water_depth < ?')
            criteriaValues << parameters['maxDepth']
        }
        if (parameters['imlgs']) {
            criteria.push('imlgs = ?')
            criteriaValues << parameters['imlgs']
        }
        if (parameters['igsn']) {
            criteria.push('igsn = ?')
            criteriaValues << parameters['igsn']
        }

        if (criteria.size() > 0) {
            println("returning ${" where ${criteria.join(' and ')}"}, ${criteriaValues}")
            return [" where ${criteria.join(' and ')}", criteriaValues]
        } else {
            return [null, null]
        }
    }

}
