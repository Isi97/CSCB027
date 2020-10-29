import React, { Component } from 'react';
import { Card } from 'antd';

class UserEntry extends Component {
    constructor(props) {
        super(props);
    }

    componentDidMount() {

    }

    render() {
        return (
            <Card size="small" title={this.props.user.username} extra={<a href={"/admin/"+this.props.user.id}>More</a>} style={{ width: 300 }}>
                <p>Email: {this.props.user.email}</p>
                <p>Name: {this.props.user.name}</p>
            </Card>
        );
    }
}

export default UserEntry