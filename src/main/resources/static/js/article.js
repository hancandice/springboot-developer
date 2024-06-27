const deleteButton = document.getElementById('delete-btn');

if (deleteButton) {
  deleteButton.addEventListener('click', event => {
    let id = document.getElementById('article-id').value;
    fetch(`/api/articles/${id}`, {
      method: 'DELETE'
    })
    .then(() => {
      alert('Deletion is done');
      location.replace('/articles');
    });
  });
}

const modifyButton = document.getElementById('save-article-btn');

if (modifyButton) {
  modifyButton.addEventListener('click', function() {
                     const id = document.getElementById('article-id').value;
                     const title = document.getElementById('article-title').value;
                     const content = document.getElementById('article-content').value;

                     const article = {
                         id: id || null,
                         title: title,
                         content: content
                     };

                     const url = id ? `/api/articles/${id}` : '/api/articles';
                     const method = id ? 'PUT' : 'POST';

                     fetch(url, {
                         method: method,
                         headers: {
                             'Content-Type': 'application/json'
                         },
                         body: JSON.stringify(article)
                     })
                     .then(response => {
                         if (response.ok) {
                             alert('Article saved successfully');
                             window.location.href = '/articles';
                         } else {
                             alert('Failed to save article');
                         }
                     })
                     .catch(error => {
                         console.error('Error:', error);
                         alert('An error occurred while saving the article');
                     });
                 });
}
