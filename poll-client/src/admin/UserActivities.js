import React, { Component } from 'react';

import { getUserActivities, deleteAd, deleteComment, deleteRating, promoteUser, banUser, unbanUser } from "../util/APIUtils"
import { Card, Button, notification } from 'antd';

class UserActivities extends Component {
    constructor(props) {
        super(props);

        this.state = {
            user: []
        }

        this.prepareActivities = this.prepareActivities.bind(this);
        this.fdeleteAd = this.fdeleteAd.bind(this);
        this.fdeleteComment = this.fdeleteComment.bind(this);
        this.fdeleteRating = this.fdeleteRating.bind(this);

        this.fbanUser = this.fbanUser.bind(this);
        this.fpromoteUser = this.fpromoteUser.bind(this);
        this.funbanUser = this.funbanUser.bind(this);

        this.loadUserActivities = this.loadUserActivities.bind(this);
    }

    componentDidMount() {
        this.loadUserActivities();
    }

    loadUserActivities() {
        getUserActivities(this.props.match.params.uid).then((response) => {
            this.setState({
                user: response,
                user_id: this.props.match.params.uid,
                loaded: true
            })

            this.prepareActivities();
        })
    }

    fdeleteAd(id) {
        deleteAd(id).then(() => {
            notification.success({
                message: "Ad deleted"
            });
            this.loadUserActivities();
        })
    }

    fdeleteComment(id) {
        deleteComment(id).then(() => {
            notification.success({
                message: "Comment deleted"
            });

            this.loadUserActivities();
        })
    }

    fdeleteRating(id) {
        deleteRating(id).then(() => {
            notification.success({
                message: "Rating deleted"
            });
            this.loadUserActivities();

        });
    }

    fbanUser(id) {
        banUser(id).then(() => {
            notification.success({
                message: "User banned"
            });
            this.loadUserActivities();
        })
    }

    fpromoteUser(id) {
        promoteUser(id).then(() => {
            notification.success({
                message: "User promoted to moderator"
            });
            this.loadUserActivities();
        });
    }

    funbanUser(id) {
        unbanUser(id).then(() => {
            notification.success({
                message: "User unbanned"
            });
            this.loadUserActivities();
        });
    }

    prepareActivities() {
        const function_deleteAd = this.fdeleteAd.bind(this);
        const function_deleteComment = this.fdeleteComment.bind(this);
        const function_deleteRating = this.fdeleteRating.bind(this);

        if (this.state.loaded) {
            let adds = [];
            const temp = this.state.user.ads;

            adds.push(<h1>Ads</h1>);
            temp.forEach(element => {
                adds.push(
                    <div className="user_ads">
                        <Card size="small" title={element.title} style={{ width: 300 }}>
                            <p>Description: {element.description}</p>
                            <Button type="dashed" onClick={function () { return function_deleteAd(element.id) }} >
                                Delete Ad
                        </Button>
                        </Card><br></br>
                    </div>
                )
            });

            let comments = [];
            comments.push(<h1>Comments</h1>)

            this.state.user.comments.forEach(element => {
                comments.push(
                    <div className="user_comments">
                        <Card size="small" title={"Comment"} style={{ width: 300 }}>
                            <p>{element.text}</p>
                            <Button type="dashed" onClick={function () { return function_deleteComment(element.id) }} >
                                Delete Comment
                        </Button>
                        </Card> <br></br>
                    </div>
                );
            });


            let ratings = [];
            ratings.push(<h1>Ratings</h1>)

            this.state.user.ratings.forEach(element => {
                ratings.push(
                    <div className="user_ratings">
                        <Card size="small" title={"Rating"} style={{ width: 300 }}>
                            <p>{element.username + " rated the ad " + element.ad_title + " with a score of " + element.value}</p>
                            <Button type="dashed" onClick={function () { return function_deleteRating(element.rating_id) }} >
                                Delete Rating
                        </Button>
                        </Card> <br></br>
                    </div>
                );
            });



            let adminButtons = []

            if (this.props.currentUser.authorities.includes("ROLE_MODERATOR")) {
                if (!this.state.user.authorities.includes("ROLE_MODERATOR")) {
                    adminButtons.push(
                        <Button onClick={() => { this.fpromoteUser(this.state.user_id) }}>Promote</Button>
                    );
                }

                if (this.state.user.authorities.includes("ROLE_BANNED")) {
                    adminButtons.push(
                        <Button onClick={() => { this.funbanUser(this.state.user_id) }}>Unban</Button>
                    );
                } else {
                    adminButtons.push(
                        <Button onClick={() => { this.fbanUser(this.state.user_id) }}>Ban</Button>
                    );
                }
            }

            this.setState({
                user_ratings: ratings,
                user_comments: comments,
                user_ads: adds,
                user_adminButtons: adminButtons
            })
        }
    }

    render() {

        return (
            <div className="user_activities">
                {this.state.user_adminButtons}
                {this.state.user_ads}
                {this.state.user_comments}
                {this.state.user_ratings}
            </div>
        );
    }
}

export default UserActivities