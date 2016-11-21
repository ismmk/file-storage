import {
  GET_COMMENTS_SUCCESS,
} from '../constants/ActionTypes'


export const initialState = {
  comments : []
}


export default function list(state = initialState, action) {
  switch (action.type) {
    case GET_COMMENTS_SUCCESS:
      return {
         ...state,
         comments : action.value
      }
      break;
    default:
      return state
  }
}
