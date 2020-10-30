import "./AdPage.css"

import React, { Component } from 'react';
import { getAd, getGallery, deleteAd, postComment, getAdComments, postRating } from "../util/APIUtils"
import { Carousel, Button, notification, Comment, List, Rate, Form, Input} from 'antd';
import { withRouter } from 'react-router-dom';

const { TextArea } = Input

class AdPage extends Component {
    constructor(props) {
        super(props);

        this.state = {
            loaded: this.props.preloaded,
            hasImages: false,
            imageList: []
        };

        this.checkImages = this.checkImages.bind(this);
        this.editAdButton = this.editAdButton.bind(this);
        this.checkCategories = this.checkCategories.bind(this);
        this.deleteAdButton = this.deleteAdButton.bind(this);
        this.checkComments = this.checkComments.bind(this);
        this.onNewComment = this.onNewComment.bind(this);
        this.reloadComments = this.reloadComments.bind(this);
        this.onRatingChange = this.onRatingChange.bind(this);
    }

    componentDidMount() {
        if (this.props.preloaded == false) {
            let stateChange = this.setState.bind(this);

            getAd(this.props.match.params.aid).then((response) => {
                stateChange({
                    loaded: true,
                    ad: response
                });
            });
        } else {
            this.checkImages();
            this.checkCategories();
            this.checkComments();
        }

        if(this.props.mode) console.log(this.props.mode);
    }

    checkComments() {
        let data = []
        let lcomments = this.props.ad.comments

        lcomments.forEach((c) => {
            data.push({
                author: c.author_name + "@" + c.author_username,
                avatar: null,
                content: c.text,
                datetime: c.createdAt.slice(0, 10)
            });
        });

        this.setState({
            comments: data
        })
    }

    reloadComments() {
        let data = []
        getAdComments(this.props.ad.id).then((response) => {
            response.forEach((c) => {
                data.push({
                    author: c.author_name + "@" + c.author_username,
                    avatar: null,
                    content: c.text,
                    datetime: c.createdAt.slice(0, 10)
                });
            });

            this.setState({
                comments: data
            })

        });
    }

    checkCategories() {
        let x = this.props.ad.categories
        let result = []

        x.forEach(element => {
            result.push(element.name + " ")
        });

        this.setState({
            adCategories: result
        })
    }

    checkImages() {
        let stateChange = this.setState.bind(this);

        getGallery(this.props.ad.id).then((response) => {
            let temp = []


            response.forEach(element => {


                let srcString = 'data:' + element.type + ';base64,' + element.data;
                temp.push(
                    <div className=""><img className="carouselImage" src={srcString}></img></div>
                );

            });


            stateChange({
                hasImages: true,
                imageList: temp
            });
        });
    }

    editAdButton(event) {
        this.props.history.push("/ads/edit/" + this.props.ad.id);
    }


    deleteAdButton(event) {
        let stateChange = this.setState.bind(this);

        deleteAd(this.props.ad.id).then((response) => {
            notification.success({
                message: "Ad deleted"
            });
            stateChange({
                deleted: true
            })
        });
    }

    onNewComment(event) {
        event.preventDefault();

        let commentRequest = {
            adId: this.props.ad.id,
            text: event.target.text.value
        }

        let cc = this.reloadComments;
        const levent = this.event;

        postComment(commentRequest).then((response) => {
            notification.success({
                message: "Comment posted!"
            });
            cc();
        });
    }

    onRatingChange(value)
    {
        let ratingRequest = {
            userId: this.props.currentUser.id,
            value: value,
            adId: this.props.ad.id
        }

        postRating(ratingRequest).then((response)=>{
            notification.success({
                message: "You rated this ad!"
            });
            this.setState({
                alreadyRated: true,
                ratedValue: value
            })
        })
    }

    render() {
        if (this.state.loaded) {
            if (this.props.preloaded) {
                if (this.state.deleted) return null;
                return (
                    <div className="ad-container">
                        <h2 className="ad-title">{this.props.ad.title}</h2>
                        <div className="ad-description">{this.props.ad.description}</div>

                        <Carousel autoplay>
                            {this.state.imageList}
                        </Carousel>
                        <div className="ad-created">Posted on {this.props.ad.createdAt.slice(0, 10)} by {this.props.ad.user.name}</div>
                        <br></br>
                        <div>
                            <p>Address: {this.props.ad.user.contactInformation.address}</p>
                            <p>Phone: {this.props.ad.user.contactInformation.phone}</p>
                        </div>
                        <div className="ad-categories">Categories: {this.state.adCategories}</div>
                        {  this.props.currentUser ? (this.props.currentUser.id == this.props.ad.user.id ? <div className="editButtonContainer"><br></br> <Button onClick={this.editAdButton} >Edit Ad</Button></div> : "") : ""}
                        {  this.props.currentUser ? (this.props.currentUser.id == this.props.ad.user.id ? <div className="deleteButtonContainer"><br></br> <Button onClick={this.deleteAdButton} >Delete Ad</Button></div> : "") : ""}
                        {this.state.comments && !this.props.mode ?
                            <List
                                className="comment-list"
                                header={`${this.state.comments.length} replies`}
                                itemLayout="horizontal"
                                dataSource={this.state.comments}
                                renderItem={item => (
                                    <div className="adComent">
                                        <li>
                                            <Comment
                                                actions={item.actions}
                                                author={item.author}
                                                avatar={item.avatar}
                                                content={item.content}
                                                datetime={item.datetime}
                                            />
                                        </li>
                                    </div>
                                )}
                            />
                            : ""}
                        {this.props.currentUser && !this.props.mode ?
                            <div className="newCommentContainer">
                                <Form onSubmit={this.onNewComment}>
                                    <Form.Item>
                                        <TextArea
                                            style={{ fontSize: '16px' }}
                                            autosize={{ minRows: 3, maxRows: 6 }}
                                            name="text"
                                        />
                                    </Form.Item>
                                    <Form.Item >
                                        <Button block type="primary" htmlType="submit">Comment</Button>
                                    </Form.Item>
                                </Form>
                            </div>
                            : "Log in to comment on ads"}
                        { !this.props.ad.ratings.hasUserRated && !this.state.alreadyRated && !this.props.mode ?
                            <div>
                                <Rate onChange={this.onRatingChange} /> total ratings: {this.props.ad.ratings.totalRatings}
                            </div>
                            :
                            <div> 
                            <Rate value={this.props.ad.ratings.averageRating || this.state.ratedValue} disabled="true" allowHalf="true"/> total ratings: {this.props.ad.ratings.totalRatings}
                            </div>
                            }
                    </div>
                );
            }
            else {
                return (
                    <div className="ad-container">
                        <h2>{this.state.ad.title}</h2>
                        <div className="ad-descritpion">{this.state.ad.description}</div>
                        <div className="ad-created">Posted on {this.state.ad.createdAt.slice(0, 10)} by {this.state.ad.user.username}</div>

                    </div>
                );
            }

        } else {
            return (<p>nothing to display</p>);
        }
    }
}

export default withRouter(AdPage)