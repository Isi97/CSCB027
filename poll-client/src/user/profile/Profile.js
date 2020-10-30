import React, { Component } from 'react';
import { getUserProfile, updateContactInformation } from '../../util/APIUtils';
import { Form, Input, Button, Avatar, Tabs, notification, Col, Row } from 'antd';
import { getAvatarColor } from '../../util/Colors';
import { formatDate } from '../../util/Helpers';
import LoadingIndicator from '../../common/LoadingIndicator';
import './Profile.css';
import NotFound from '../../common/NotFound';
import ServerError from '../../common/ServerError';
import AdList from '../../ads/AdList';

const TabPane = Tabs.TabPane;

class Profile extends Component {
    constructor(props) {
        super(props);
        this.state = {
            user: null,
            isLoading: false
        }
        this.loadUserProfile = this.loadUserProfile.bind(this);
        this.handleContactSubmit = this.handleContactSubmit.bind(this);
    }

    loadUserProfile(username) {
        this.setState({
            isLoading: true
        });

        getUserProfile(username)
            .then(response => {
                this.setState({
                    user: response,
                    isLoading: false
                });
            }).catch(error => {
                if (error.status === 404) {
                    this.setState({
                        notFound: true,
                        isLoading: false
                    });
                } else {
                    this.setState({
                        serverError: true,
                        isLoading: false
                    });
                }
            });
    }

    handleContactSubmit(event) {
        event.preventDefault();

        const contactInformation = {
            id: -1,
            address: event.target.address.value,
            phone: event.target.phone.value
        };


        updateContactInformation(this.state.user.username, contactInformation)
            .then(response => {
                this.setState({
                    contactsUpdated: true
                });
                notification.success({
                    message: "Contacts Updated"
                });
            }).catch(error => {
                this.setState({
                    serverError: true,
                    isLoading: false
                });
            })

    }

    componentDidMount() {
        const username = this.props.match.params.username;
        this.loadUserProfile(username);
    }

    componentDidUpdate(nextProps) {
        if (this.props.match.params.username !== nextProps.match.params.username) {
            this.loadUserProfile(nextProps.match.params.username);
        }
    }

    render() {
        if (this.state.isLoading) {
            return <LoadingIndicator />;
        }

        if (this.state.notFound) {
            return <NotFound />;
        }

        if (this.state.serverError) {
            return <ServerError />;
        }

        const tabBarStyle = {
            textAlign: 'center'
        };

        return (
            <div className="profile">
                {
                    this.state.user ? (
                        <div className="user-profile">
                            <div className="user-details">
                                <div className="user-avatar">
                                    <Avatar className="user-avatar-circle" style={{ backgroundColor: getAvatarColor(this.state.user.name) }}>
                                        {this.state.user.name[0].toUpperCase()}
                                    </Avatar>
                                </div>
                                <div className="user-summary">
                                    <div className="full-name">{this.state.user.name}</div>
                                    <div className="username">@{this.state.user.username}</div>
                                    <div className="user-joined">
                                        Joined {formatDate(this.state.user.joinedAt)}
                                    </div>
                                </div>
                            </div>

                            <div className="user-poll-details">
                                <Tabs defaultActiveKey="1"
                                    animated={false}
                                    tabBarStyle={tabBarStyle}
                                    size="large"
                                    className="profile-tabs">
                                    <TabPane tab='My Ads' key="1">
                                        <AdList currentUser={this.state.user} mode="my" />
                                    </TabPane>
                                    <TabPane tab="My Contact Information" key="2">
                                        <div className="user-details">
                                            <h1 style={{ textAlign: "center" }}>Contact Details</h1><br />

                                            <Form onSubmit={this.handleContactSubmit} className="contact-information-form" layout="vertical" size="large" labelCol={{ span: 8 }} wrapperCol={{ span: 16 }} >
                                                <Row>
                                                    <Form.Item label="Address">

                                                        <Input label="test" name="address" defaultValue={this.state.user.contactInformation === null ? "" : this.state.user.contactInformation.address} />
                                                    </Form.Item>
                                                </Row>
                                                <Row >
                                                    <Form.Item label="Phone">
                                                        <Input name="phone" defaultValue={this.state.user.contactInformation === null ? "" : this.state.user.contactInformation.phone} />
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
                                        </div>
                                    </TabPane>
                                </Tabs>
                            </div>
                        </div>
                    ) : null
                }
            </div>
        );
    }
}

export default Profile;