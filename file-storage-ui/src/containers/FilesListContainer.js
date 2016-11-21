import React, { Component, PropTypes} from 'react'
import { connect } from 'react-redux'
import { updateFilters, getFiles, uploadFile, changeSortDirection } from '../actions'
import FileSearch from '../components/FileSearch'
import FileUpload from  '../components/FileUpload'
import ReactDataGrid from 'react-data-grid'
import columns from './fileColumns'


class FilesListContainer extends Component {

    componentDidMount () {
      const {
        pressSearch,
        filter
      } = this.props
      pressSearch(filter)
    }
    render () {
     const {
       items,
       pressSearch,
       onFileUpload,
       changeSort,
       filter
     } = this.props
    return (
      <div>
        <FileUpload onUpload={onFileUpload}/>
        <FileSearch
          onSearch={pressSearch}
          filter={filter}
        />
        <ReactDataGrid
         columns={columns}
         onGridSort = {changeSort}
         rowGetter={rowIdx => items[rowIdx]}
         rowsCount={items.length}
         minHeight={500} />
      </div>
    )
  }
}

FilesListContainer.propTypes = {
  isFetching : PropTypes.bool
}

const mapStateToProps = state => ({
  items: state.fileList.items,
  filter: state.fileList.filter
})

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    pressSearch: (search) => {
      dispatch(updateFilters(search))
      getFiles(search)(dispatch)
    },
    onFileUpload: (content) => {
      dispatch(uploadFile(content))
    },
    changeSort : (column, direction) => {
      dispatch(changeSortDirection(column, direction))
    }
  }
}

export default connect( mapStateToProps, mapDispatchToProps)(FilesListContainer)
