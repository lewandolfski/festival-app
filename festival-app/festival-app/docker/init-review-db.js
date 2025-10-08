// Review Service MongoDB Initialization Script
// Creates collections and sample data for the Review Service

// Switch to review database
db = db.getSiblingDB('review_db');

// Create review collection with validation
db.createCollection('reviews', {
   validator: {
      $jsonSchema: {
         bsonType: 'object',
         required: ['subjectId', 'subjectType', 'reviewerName', 'rating'],
         properties: {
            subjectId: {
               bsonType: 'string',
               description: 'Subject ID is required and must be a string'
            },
            subjectType: {
               bsonType: 'string',
               enum: ['DJ', 'PERFORMANCE'],
               description: 'Subject type must be either DJ or PERFORMANCE'
            },
            reviewerName: {
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
            createdAt: {
               bsonType: 'date',
               description: 'Creation timestamp'
            },
            updatedAt: {
               bsonType: 'date',
               description: 'Update timestamp'
            }
         }
      }
   }
});

// Create indexes for better performance
db.reviews.createIndex({ 'subjectId': 1, 'subjectType': 1 });
db.reviews.createIndex({ 'subjectType': 1 });
db.reviews.createIndex({ 'reviewerName': 1 });
db.reviews.createIndex({ 'rating': 1 });
db.reviews.createIndex({ 'createdAt': -1 });

// Insert sample reviews
db.reviews.insertMany([
   {
      subjectId: '1',
      subjectType: 'DJ',
      reviewerName: 'Alice Johnson',
      rating: 5,
      comment: 'Amazing performance! DJ Thunderbolt really knows how to get the crowd moving.',
      createdAt: new Date(),
      updatedAt: new Date()
   },
   {
      subjectId: '2',
      subjectType: 'DJ',
      reviewerName: 'Bob Smith',
      rating: 4,
      comment: 'Great house music selection. DJ Harmony created the perfect atmosphere.',
      createdAt: new Date(),
      updatedAt: new Date()
   },
   {
      subjectId: '1',
      subjectType: 'PERFORMANCE',
      reviewerName: 'Carol Davis',
      rating: 5,
      comment: 'Electric Night Opening was absolutely spectacular! Perfect way to start the festival.',
      createdAt: new Date(),
      updatedAt: new Date()
   },
   {
      subjectId: '3',
      subjectType: 'DJ',
      reviewerName: 'David Wilson',
      rating: 4,
      comment: 'DJ Vortex brought some serious techno energy. Underground vibes were perfect.',
      createdAt: new Date(),
      updatedAt: new Date()
   },
   {
      subjectId: '2',
      subjectType: 'PERFORMANCE',
      reviewerName: 'Emma Brown',
      rating: 5,
      comment: 'House Party Vibes was exactly what I needed. Great music and atmosphere.',
      createdAt: new Date(),
      updatedAt: new Date()
   }
]);

// Create user for the application (if not exists)
try {
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
} catch (error) {
   // User might already exist, ignore error
   print('User creation skipped - user might already exist');
}

print('Review database initialization completed successfully!');
print('Collections created: reviews');
print('Sample data inserted: 5 reviews');
print('Indexes created for optimal performance');
