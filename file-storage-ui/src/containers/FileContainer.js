import React, { Component, PropTypes} from 'react'
import { connect } from 'react-redux'
import { getComments, addComment } from '../actions'
import { Link } from 'react-router'


class FileContainer extends Component {
    componentDidMount () {
      const {
        getComments
      } = this.props
      getComments(this.props.params.fileId)
    }
    render () {
     const {
       comments,
       addComment
     } = this.props
    var comment = {}
    return (
      <div>
        <Link to={'/'}>Back</Link>
        <br/>
        {
          comments.map(function(o) {
            return <div>{o.text}</div>
           })
        }
        <input type = "text" name = "newComment" onChange={evt => comment.data = evt.target.value}/>
        <input type = "button" name="addComent" onClick={() => addComment(this.props.params.fileId, comment.data)} value = "Add Comment"/>
      </div>
    )
  }
}

FileContainer.propTypes = {
  isFetching : PropTypes.bool
}

const mapStateToProps = state => ({
  comments: state.file.comments
})

const mapDispatchToProps = (dispatch, ownProps) => {
  return {
    getComments: (fileId) => {
      getComments(fileId)(dispatch)
    },
    addComment: (fileId, content) => {
      dispatch(addComment(fileId, content, () => getComments(fileId)(dispatch) ))
    }
  }
}

export default connect(mapStateToProps, mapDispatchToProps)(FileContainer)
