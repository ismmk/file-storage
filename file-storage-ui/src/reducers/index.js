import { combineReducers } from 'redux'
import fileList from './fileList'
import file from './file'
import { routerReducer } from 'react-router-redux'


export default combineReducers({
  routing : routerReducer,
  fileList,
  file
})
