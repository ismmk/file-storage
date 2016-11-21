import React, { Component } from 'react'
import { Link } from 'react-router'

class DownloadLinkFormatter extends Component {
  render () {
    return (<a href={this.props.value.find(function(o) {  return o.rel == 'download'}).href}>
      Download
    </a>)
  }
}
class CommentsLinkFormatter extends Component {
  render () {
    return (<Link to={`/file/${this.props.value}`}>Comments</Link>)
  }
}

const columns = [
{
  key : 'links',
  name: 'Download',
  sortable : false,
  formatter : DownloadLinkFormatter
},
{
  key: 'name',
  name: 'Name',
  sortable : true
},{
  key: 'contentType',
  name: 'Content Type',
  sortable : true
},{
  key: 'uploadTimeDate',
  name: 'Upload Time',
  sortable : false
},
{
  key : 'fileId',
  name: 'Comments',
  sortable : false,
  formatter : CommentsLinkFormatter
}]

export default columns
