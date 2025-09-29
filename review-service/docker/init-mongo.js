// MongoDB initialization script for Review Service
// This script creates the review database and user

// Switch to the review database
db = db.getSiblingDB('review_db');

// Create the review_user with read/write permissions
db.createUser({
  user: 'review_user',
  pwd: 'review_pass',
  roles: [
    {
      role: 'readWrite',
      db: 'review_db'
    }
  ]
});

// Create the reviews collection with validation
db.createCollection('reviews', {
  validator: {
    $jsonSchema: {
      bsonType: 'object',
      required: ['subject_id', 'subject_type', 'reviewer_name', 'rating'],
      properties: {
        subject_id: {
          bsonType: 'string',
          description: 'Subject ID is required and must be a string'
        },
        subject_type: {
          bsonType: 'string',
          enum: ['DJ', 'PERFORMANCE'],
          description: 'Subject type must be either DJ or PERFORMANCE'
        },
        reviewer_name: {
          bsonType: 'string',
          minLength: 2,
          maxLength: 100,
          description: 'Reviewer name must be between 2 and 100 characters'
        },
        rating: {
          bsonType: 'int',
          minimum: 1,
          maximum: 5,
          description: 'Rating must be an integer between 1 and 5'
        },
        comment: {
          bsonType: 'string',
          maxLength: 1000,
          description: 'Comment cannot exceed 1000 characters'
        },
        created_at: {
          bsonType: 'date',
          description: 'Created timestamp'
        },
        updated_at: {
          bsonType: 'date',
          description: 'Updated timestamp'
        }
      }
    }
  }
});

// Create indexes for better performance
db.reviews.createIndex({ 'subject_id': 1, 'subject_type': 1 });
db.reviews.createIndex({ 'subject_type': 1 });
db.reviews.createIndex({ 'reviewer_name': 1 });
db.reviews.createIndex({ 'rating': 1 });
db.reviews.createIndex({ 'created_at': -1 });

// Insert sample reviews for testing
db.reviews.insertMany([
  {
    subject_id: 'dj-1',
    subject_type: 'DJ',
    reviewer_name: 'Alice Johnson',
    rating: 5,
    comment: 'Amazing performance! Great music selection and energy.',
    created_at: new Date(),
    updated_at: new Date()
  },
  {
    subject_id: 'dj-1',
    subject_type: 'DJ',
    reviewer_name: 'Bob Smith',
    rating: 4,
    comment: 'Really good DJ, crowd loved the set.',
    created_at: new Date(),
    updated_at: new Date()
  },
  {
    subject_id: 'performance-1',
    subject_type: 'PERFORMANCE',
    reviewer_name: 'Carol Davis',
    rating: 5,
    comment: 'Incredible show! The atmosphere was electric.',
    created_at: new Date(),
    updated_at: new Date()
  },
  {
    subject_id: 'dj-2',
    subject_type: 'DJ',
    reviewer_name: 'David Wilson',
    rating: 3,
    comment: 'Decent performance, but could be better.',
    created_at: new Date(),
    updated_at: new Date()
  },
  {
    subject_id: 'performance-2',
    subject_type: 'PERFORMANCE',
    reviewer_name: 'Eva Martinez',
    rating: 4,
    comment: 'Great show overall, loved the lighting effects.',
    created_at: new Date(),
    updated_at: new Date()
  }
]);

print('Review database initialized successfully with sample data!');
print('Created user: review_user');
print('Created collection: reviews with validation rules');
print('Inserted 5 sample reviews');
print('Created performance indexes');
