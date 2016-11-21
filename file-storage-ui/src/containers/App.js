import React, { Component } from 'react'
import FilesListContainer from './FilesListContainer'
import FileContainer from './FileContainer'
import Main from './Main'
import { createStore, applyMiddleware } from 'redux'
import { Provider } from 'react-redux'
import createLogger from 'redux-logger'
import thunk from 'redux-thunk'
import reducer from '../reducers'
import {Router, Route, IndexRoute, browserHistory} from 'react-router'
import { syncHistoryWithStore } from 'react-router-redux'

const middleware = [ thunk ];
if (process.env.NODE_ENV !== 'production') {
  middleware.push(createLogger());
}

const store = createStore(
  reducer,
  applyMiddleware(...middleware)
)

const history = syncHistoryWithStore(browserHistory, store)

export default class App extends Component{
  render() {
    const routes = (store) => {
      return (
        <Route path = "/" component={Main}>
          <IndexRoute component={FilesListContainer}/>
          <Route path="/file/:fileId" component={FileContainer}/>
        </Route>
      )
    }

    return (
      <Provider store={store}>
        <Router history={history}>
          { routes(store) }
        </Router>
      </Provider>
    )
  }
}
