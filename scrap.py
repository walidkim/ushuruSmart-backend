import requests
from bs4 import BeautifulSoup

def get_video_links(playlist_url):
    # Send a GET request to the playlist URL
    response = requests.get(playlist_url)
    
    # Check if the request was successful
    if response.status_code == 200:
        # Parse the HTML content of the page
        soup = BeautifulSoup(response.content, 'html.parser')
        
        # Find all elements with class 'yt-simple-endpoint' which contains video links
        video_links = soup.find_all('div', {'class': "style-scope ytd-playlist-video-list-renderer"})
        print(video_links)
        
        # Extract the href attribute from each link and store them in a list
        video_urls = [link['href'] for link in video_links]
        
        # Convert relative URLs to absolute URLs
        video_urls = ['https://www.youtube.com' + url for url in video_urls]
        
        return video_urls
    else:
        # If the request was not successful, print an error message
        print("Error: Unable to retrieve playlist. Status code:", response.status_code)
        return []

# Example usage:
playlist_url = 'https://www.youtube.com/playlist?list=PLEJXowNB4kPxBwaXtRO1qFLpCzF75DYrS'
video_links = get_video_links(playlist_url)
if video_links:
    print("List of video links in the playlist:")
    for link in video_links:
        print(link)
else:
    print("No video links found.")
