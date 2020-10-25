import { Upload, message, Button, notification } from 'antd';
import React, { Component } from 'react';
import {postFile} from './APIUtils';

class UploadForm extends Component {
    sett = {};

    constructor(props) {
        super(props);
        this.state = {
            fileList: [],
            downloading: false
        }

        this.handleFormUpload = this.handleFormUpload.bind(this);


    }

    handleFormUpload() {
        const { fileList } = this.state;
        const formData = new FormData();

        formData.append('adId', this.props.adId);

        fileList.forEach(file => {
            formData.append('filekey', file);
        });

        this.setState({
            uploading: true,
        });

        postFile(formData);

        this.setState({
            uploading: false,
            fileList: []
        });

        notification.success({
            message: "Image uploaded to this ad's gallery."
        });
    };



    render() {
        const { uploading, fileList } = this.state;
        const props = {
            onRemove: file => {
                this.setState(state => {
                    const index = state.fileList.indexOf(file);
                    const newFileList = state.fileList.slice();
                    newFileList.splice(index, 1);
                    return {
                        fileList: newFileList,
                    };
                });
            },
            beforeUpload: file => {
                this.setState(state => ({
                    fileList: [...state.fileList, file],
                }));
                return false;
            },
            fileList,
        };

        return (
            <div className="fileUpload">
                <Upload {...props}  showUploadList={false}>
                    <Button >Select Image</Button>
                </Upload>
                <Button
                    type="primary"
                    onClick={this.handleFormUpload}
                    disabled={this.state.fileList.length === 0}
                    loading={this.state.uploading}
                    style={{ marginTop: 16 }}
                >
                    {uploading ? 'Uploading' : 'Start Upload'}
                </Button>
            </div>
        );
    }

}

export default UploadForm