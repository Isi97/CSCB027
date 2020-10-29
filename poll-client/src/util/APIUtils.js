import { API_BASE_URL, POLL_LIST_SIZE, ACCESS_TOKEN } from '../constants';

const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    })
    
    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
    .then(response => 
        response.json().then(json => {
            if(!response.ok) {
                return Promise.reject(json);
            }
            return json;
        })
    );
};

const multiPartRequest = (options) => {
    const headers = new Headers({
        //'Content-Type': 'application/json',
    })
    
    if(localStorage.getItem(ACCESS_TOKEN)) {
        headers.append('Authorization', 'Bearer ' + localStorage.getItem(ACCESS_TOKEN))
    }

    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);

    return fetch(options.url, options)
    .then(response => 
        response.json().then(json => {
            if(!response.ok) {
                return Promise.reject(json);
            }
            return json;
        })
    );
};

export function getAllPolls(page, size) {
    page = page || 0;
    size = size || POLL_LIST_SIZE;

    return request({
        url: API_BASE_URL + "/polls?page=" + page + "&size=" + size,
        method: 'GET'
    });
}

export function getAllAds(page, size)
{
    page = page || 0;
    size = size || POLL_LIST_SIZE;

    return request({
        url: API_BASE_URL + "/ads?page=" + page + "&size=" + size,
        method: 'GET'
    });
}

export function getAdCategories()
{
    return request({
        url: API_BASE_URL + "/ads/categories",
        method: 'GET'
    }); 
}

export function createPoll(pollData) {
    return request({
        url: API_BASE_URL + "/polls",
        method: 'POST',
        body: JSON.stringify(pollData)         
    });
}

export function castVote(voteData) {
    return request({
        url: API_BASE_URL + "/polls/" + voteData.pollId + "/votes",
        method: 'POST',
        body: JSON.stringify(voteData)
    });
}

export function login(loginRequest) {
    return request({
        url: API_BASE_URL + "/auth/signin",
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
}

export function signup(signupRequest) {
    return request({
        url: API_BASE_URL + "/auth/signup",
        method: 'POST',
        body: JSON.stringify(signupRequest)
    });
}

export function checkUsernameAvailability(username) {
    return request({
        url: API_BASE_URL + "/user/checkUsernameAvailability?username=" + username,
        method: 'GET'
    });
}

export function checkEmailAvailability(email) {
    return request({
        url: API_BASE_URL + "/user/checkEmailAvailability?email=" + email,
        method: 'GET'
    });
}


export function getCurrentUser() {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/user/me",
        method: 'GET'
    });
}

export function getUserProfile(username) {
    return request({
        url: API_BASE_URL + "/users/" + username,
        method: 'GET'
    });
}

export function getAd(id) {
    return request({
        url: API_BASE_URL + "/ads/" + id,
        method: 'GET'
    });
}

export function deleteAd(id) {
    return request({
        url: API_BASE_URL + "/ads/" + id,
        method: 'DELETE'
    });
}

export function deleteComment(id) {
    return request({
        url: API_BASE_URL + "/comments/" + id,
        method: 'DELETE'
    });
}

export function deleteRating(id) {
    return request({
        url: API_BASE_URL + "/ratings/" + id,
        method: 'DELETE'
    });
}

export function getUserCreatedPolls(username, page, size) {
    page = page || 0;
    size = size || POLL_LIST_SIZE;

    return request({
        url: API_BASE_URL + "/users/" + username + "/polls?page=" + page + "&size=" + size,
        method: 'GET'
    });
}

export function getUserVotedPolls(username, page, size) {
    page = page || 0;
    size = size || POLL_LIST_SIZE;

    return request({
        url: API_BASE_URL + "/users/" + username + "/votes?page=" + page + "&size=" + size,
        method: 'GET'
    });
}

export function searchAdsByCategory(page, size, categories) {
    page = page || 0;
    size = size || POLL_LIST_SIZE;

    return request({
        url: API_BASE_URL + "/ads/search?page=" + page + "&size=" + size +"&categories=" +categories,
        method: 'GET'
    });
}

export function getFile(id)
{
    return request({
        url: API_BASE_URL + "/images/" + id,
        method: 'GET'
    });
}

export function getGallery(id)
{
    return request({
        url: API_BASE_URL + "/images/gallery/" + id,
        method: 'GET'
    });
}

export function getMyAds()
{
    return request({
        url: API_BASE_URL + "/ads/my",
        method: 'GET'
    });
}

export function updateContactInformation(user, payload) {
    if(!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: API_BASE_URL + "/users/" + user + "/contacts",
        body: JSON.stringify(payload),
        method: 'Post'
    });
}


export function postFile(formData)
{
    return multiPartRequest({
        url: API_BASE_URL + "/images",
        method: 'POST',
        body: formData,
    });
}

export function postAd(adinfo)
{
    return request({
        url: API_BASE_URL + "/ads",
        method: 'POST',
        body: JSON.stringify(adinfo)
    });
}

export function updateAd(adinfo)
{
    return request({
        url: API_BASE_URL + "/ads",
        method: 'PUT',
        body: JSON.stringify(adinfo)
    });
}

export function postComment(commentRequest)
{
    return request({
        url: API_BASE_URL + "/comments",
        method: 'POST',
        body: JSON.stringify(commentRequest)
    });
}

export function getAdComments(adId)
{
    return request({
        url: API_BASE_URL + "/comments/ad/" + adId,
        method: 'GET',
    });
}

export function postRating(ratingRequest)
{
    return request({
        url: API_BASE_URL + "/ratings",
        method: 'POST',
        body: JSON.stringify(ratingRequest)
    });
}

export function getAllUsers()
{
    return request({
        url: API_BASE_URL + "/users/all/",
        method: 'GET',
    });
}

export function getUserActivities(uid)
{
    return request({
        url: API_BASE_URL + "/user/"+uid+"/activities",
        method: 'GET',
    });
}

export function banUser(id)
{
    return request({
        url: API_BASE_URL + "/user/"+id+"/ban",
        method: 'POST',
    });
}

export function unbanUser(id)
{
    return request({
        url: API_BASE_URL + "/user/"+id+"/unban",
        method: 'POST',
    });
}

export function promoteUser(id)
{
    return request({
        url: API_BASE_URL + "/user/"+id+"/promote",
        method: 'POST',
    });
}