import React, { Component, PropTypes } from 'react'
import { autobind } from 'core-decorators'

class FileSearch extends Component {
  render() {
    const {
      onSearch,
      filter
    } = this.props

    var searchData = {
      ...filter
    }

    return (
      <div>
       Name : <input name="name" onChange={evt => searchData.name = evt.target.value} />
       Extension : <input name="extension" onChange={evt => searchData.extension = evt.target.value} />
        <input class="btn" name="search"
          type="button"
          value="Search"
          onClick={() => onSearch(searchData)}
        />
      </div>
    )
  }
}

FileSearch.propTypes = {
  onSearch : PropTypes.func,
  filter : PropTypes.shape({
    name : PropTypes.string,
    extension : PropTypes.string
  }).isRequired
}

export default FileSearch
