import {
  GET_FILES_REQUEST,
  GET_FILES_SUCCESS,
  GET_FILES_FAILURE,
  UPDATE_FILE_FILTERS,
  CHANGE_FILE_SORT_DIRECTION
} from '../constants/ActionTypes'
import { get } from 'lodash'


export const initialState = {
  filter : {
    name : '',
    extension : '',
    to : '',
    from : ''
  },
  sort : {
    direction : "ASC",
    field : "name"
  },
  items : [],
  isFething : false
}

function sortItems(items, conf) {
    var comparer = function(a, b) {
      if(conf.direction === 'ASC'){
        return (a[conf.field] > b[conf.field]) ? 1 : -1;
      }else if(conf.direction === 'DESC'){
        return (a[conf.field] < b[conf.field]) ? 1 : -1;
      }
    }
    return conf.direction === 'NONE' ? items.slice(0) : items.sort(comparer);
}

export default function fileList(state = initialState, action) {
  switch (action.type) {
    case UPDATE_FILE_FILTERS:
      return {
         ...state,
         filter : action.value
      }
      break;
    case CHANGE_FILE_SORT_DIRECTION:
      var newState = {
        ...state,
        sort : action.value,
        items : sortItems(state.items, action.value)
      }
      return newState;
    case GET_FILES_SUCCESS:
      var items = action.value.map(function(o) {
         return {
           ...o,
           uploadTimeDate : new Date(o.uploadTime).toLocaleDateString()
        }
       })
        return {
            ...state,
          items : sortItems(items, state.sort)
        }
        break;
    default:
      return state
  }
}
