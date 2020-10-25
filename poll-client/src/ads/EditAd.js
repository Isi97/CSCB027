import './NewAd.css';

import { Form, Input, Button, notification, Col, Row } from 'antd';
import React, { Component } from 'react';
import { postAd } from "../util/APIUtils"
import UploadForm from "../util/FUpload"
import { getAd } from "../util/APIUtils"


const { TextArea } = Input

class EditAd extends Component {

    constructor(props) {
        super(props);

        this.state = {
            loaded: false
        }

        this.handleSubmit = this.handleSubmit.bind(this);
    }

    componentDidMount() {

        let stateChange = this.setState.bind(this);

        getAd(this.props.match.params.adid).then((response) => {
            stateChange({
                loaded: true,
                ad: response
            });
        });

    }

    handleSubmit(event) {
        event.preventDefault();

        const adInfo = {
            title: event.target.title.value,
            description: event.target.description.value
        };

        postAd(adInfo).then((response) => {
            notification.success({
                message: "Ad updated!"
            });
        }).then((response) => {
            //this.props.history.push("/");
        });

    }

    render() {
        if (this.state.loaded) {
            return (
                <div className="new-ad-container">
                    <Form onSubmit={this.handleSubmit} className="new-ad-form" layout="vertical" size="large" labelCol={{ span: 8 }} wrapperCol={{ span: 16 }} >
                        <Row>
                            <Form.Item label="Title">
                                <Input name="title"defaultValue={this.state.ad.title} />
                            </Form.Item>
                        </Row>
                        <Row >
                            <Form.Item label="Description">
                                <TextArea
                                    style={{ fontSize: '16px' }}
                                    autosize={{ minRows: 3, maxRows: 6 }}
                                    name="description"
                                    defaultValue={this.state.ad.description}
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
                    <UploadForm adId={this.state.ad.id} />
                </div >
            )
        } else return ("no load");
    }
}

export default EditAd