import './NewAd.css';

import { Form, Input, Button, notification, Col, Row, Checkbox } from 'antd';
import React, { Component } from 'react';
import { postAd, updateAd } from "../util/APIUtils"
import UploadForm from "../util/FUpload"
import { getAd, getAdCategories } from "../util/APIUtils"


const { TextArea } = Input

class EditAd extends Component {

    constructor(props) {
        super(props);

        this.state = {
            loaded: false
        }

        this.handleSubmit = this.handleSubmit.bind(this);
        this.onChange = this.onChange.bind(this);
    }

    componentDidMount() {

        let stateChange = this.setState.bind(this);

        getAd(this.props.match.params.adid).then((response) => {

            stateChange({
                loaded: true,
                ad: response
            });

            if (response.categories) {
                let currentCategoriesL = []
                response.categories.forEach(element => {
                    currentCategoriesL.push(element.name);
                });

                console.log(currentCategoriesL);

                stateChange({
                    currentCategories: currentCategoriesL,
                    categoriesLoaded: true
                });
            }



        });

        let temp = []
        getAdCategories().then((response) => {
            response.forEach(element => {
                temp.push({label: element.name, value: element.name});
            })

            stateChange({
                categories: temp
            });
        });




    }

    handleSubmit(event) {
        event.preventDefault();

        const adInfo = {
            title: event.target.title.value,
            description: event.target.description.value,
            location: event.target.location.value,
            categories: this.state.selectedCategories,
            id: this.props.match.params.adid
        };

        console.log(adInfo);

        updateAd(adInfo).then((response) => {
            notification.success({
                message: "Ad updated!"
            });
            this.props.history.push("/");
        }).then((response) => {
            //this.props.history.push("/");
        });

    }

    onChange(checkedValues) {
        this.setState({
            selectedCategories: checkedValues
        });
    }

    render() {
        console.log(this.state.currentCategories)
        if (this.state.loaded) {
            return (
                <div className="new-ad-container">
                    <Form onSubmit={this.handleSubmit} className="new-ad-form" layout="vertical" size="large" labelCol={{ span: 8 }} wrapperCol={{ span: 16 }} >
                        <Row>
                            <Form.Item label="Title">
                                <Input name="title" defaultValue={this.state.ad.title} />
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
                        <Row>
                            <Form.Item label="location">
                                <Input name="location" defaultValue={this.state.ad.location || ""} />
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
                    { this.state.categoriesLoaded ? 
                    <Checkbox.Group
                        options={this.state.categories}
                        defaultValue={this.state.currentCategories}
                        onChange={this.onChange}
                    />
                    : "" }
                    <UploadForm adId={this.state.ad.id} />
                </div >
            )
        } else return ("no load");
    }
}

export default EditAd