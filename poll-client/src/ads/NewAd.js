import './NewAd.css';

import { Form, Input, Button, notification, Col, Row } from 'antd';
import React, { Component } from 'react';
import { postAd } from "../util/APIUtils"
import UploadForm from "../util/FUpload"


const { TextArea } = Input

class NewAd extends Component {

    constructor(props) {
        super(props);

        this.state = {
            test: true
        }


        this.handleSubmit = this.handleSubmit.bind(this);
    }


    handleSubmit(event) {
        event.preventDefault();

        const adInfo = {
            title: event.target.title.value,
            description: event.target.description.value
        };

        postAd(adInfo).then((response) => {
            notification.success({
                message: "Ad posted!"
            });
            let aid = response.message.replace(/(^\d+)(.+$)/i,'$1')
            this.props.history.push("/ads/edit/"+aid);
        });

    }

    render() {
        return (
            <div className="new-ad-container">
                <Form onSubmit={this.handleSubmit} className="new-ad-form" layout="vertical" size="large" labelCol={{ span: 8 }} wrapperCol={{ span: 16 }} >
                    <Row>
                        <Form.Item label="Title">
                            <Input name="title" />
                        </Form.Item>
                    </Row>
                    <Row >
                        <Form.Item label="Description">
                            <TextArea
                                style={{ fontSize: '16px' }}
                                autosize={{ minRows: 3, maxRows: 6 }}
                                name="description"
                            />
                        </Form.Item>
                    </Row>
                    <Row >
                        <Col span={24} offset={4}>
                            <Form.Item >
                                <Button block type="primary" htmlType="submit">Update</Button>
                            </Form.Item>
                        </Col>
                    </Row>
                </Form>
            </div >
        )
    }
}

export default NewAd