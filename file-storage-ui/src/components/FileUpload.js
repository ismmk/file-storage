import React, { Component, PropTypes } from 'react'
import FileInput from 'react-file-input'

class FileUpload extends Component {
  handleChange (holder) {
    return evt => holder.content = evt.target.files[0]
  }
  render() {
    const {
      onUpload
    } = this.props
    var file = {}
    return (
      <div>
       <form>
         <FileInput name="file"
                 placeholder="File to upload"
                 className="inputClass"
                 onChange={this.handleChange(file)} />
        <input type="button" name="upload" onClick={() => onUpload(file.content)} value="Upload"/>
       </form>
      </div>
    )
  }
}

FileUpload.propTypes = {
  onUpload : PropTypes.func.isRequired
}

export default FileUpload
