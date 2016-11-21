import {
  UPDATE_FILE_FILTERS,
  GET_FILES_REQUEST,
  GET_FILES_SUCCESS,
  GET_FILES_FAILURE,
  UPLOAD_FILE_REQUEST,
  UPLOAD_FILE_SUCCESS,
  CHANGE_FILE_SORT_DIRECTION,
  GET_COMMENTS_REQUEST,
  GET_COMMENTS_SUCCESS,
  ADD_COMMENT_SUCCESS,
  ADD_COMMENT_REQUEST
} from '../constants/ActionTypes'
import URI from 'urijs'

// TODO change to /proxy and add route in nginx 
const url = 'http://localhost:8082'

export function updateFilters(props) {
  return {
    type : UPDATE_FILE_FILTERS,
    value : props
  }
}
export function changeSortDirection(column, dir) {
  return {
    type : CHANGE_FILE_SORT_DIRECTION,
    value : {
      field : column,
      direction : dir
    }
  }
}
export function uploadFile(file) {
  return dispatch => {
    dispatch({
      type : UPLOAD_FILE_REQUEST,
      value : file
    })

    var formData  = new FormData();
    formData.append("file", file);


   fetch(url + '/api/files/', {
     method: 'POST',
     body: formData
   }).then(json => dispatch({
     type : UPLOAD_FILE_SUCCESS,
     value : file
   }))
 }
}



export function getFiles(data){
  return dispatch => {
    dispatch({
      type : GET_FILES_REQUEST,
      value : data
    })

    return fetch(URI(url + '/api/files/').query(data).normalize().toString())
     .then(response => response.json())
     .then(json => dispatch({
       type : GET_FILES_SUCCESS,
       value : json
     }))
  }
}


export function addComment(fileId, comment, callback) {
  return dispatch => {
    dispatch({
      type : ADD_COMMENT_REQUEST,
      value : comment
    })

   fetch(url + '/api/files/' + fileId + "/comments/", {
     method: 'POST',
     body: comment
   }).then(json =>{
     dispatch({
       type : ADD_COMMENT_SUCCESS,
       value : comment
     })
     callback()
   } )
 }
}

export function getComments(fileId){
  return dispatch => {
    dispatch({
      type : GET_COMMENTS_REQUEST,
      value : fileId
    })

    return fetch(url + '/api/files/' + fileId + "/comments/")
     .then(response => response.json())
     .then(json => dispatch({
       type : GET_COMMENTS_SUCCESS,
       value : json
     }))
  }
}
