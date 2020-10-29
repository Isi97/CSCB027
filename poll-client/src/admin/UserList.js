import React, { Component } from 'react';
import { getAllUsers, promoteUser, banUser, unbanUser } from "../util/APIUtils";
import UserEntry from "./UserEntry";

import { Button, notification } from 'antd';


class UserList extends Component {
    constructor(props) {
        super(props);

        this.state = {
            currentUser: this.props.currentUser,
            users: []
        };

    }

    componentDidMount() {
        let stateChange = this.setState.bind(this);
        

        getAllUsers().then((response) => {
            let data = []
            data.push(<br></br>)
            const test = this.state.currentUser
            console.log(test)

            response.forEach(element => {
                data.push(
                    <div>
                        <UserEntry user={element} /><br></br>
                    </div>
                )

            });



            stateChange({
                users: data
            });
        })
    }

    render() {

        return (
            <div className="user_list">
                {this.state.users}
            </div>
        );
    }
}

export default UserList