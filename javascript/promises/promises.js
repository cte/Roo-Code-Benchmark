//
// This is only a SKELETON file for the 'Promises' exercise. It's been provided as a
// convenience to get you started writing code faster.
//

/**
 * Converts a callback-style function to a Promise-based function
 * @param {Function} fn - The callback-style function to promisify
 * @returns {Function} A function that returns a Promise
 */
export const promisify = (fn) => {
  return function(...args) {
    return new Promise((resolve, reject) => {
      fn(...args, (error, result) => {
        if (error) {
          reject(error);
        } else {
          resolve(result);
        }
      });
    });
  };
};

/**
 * Mimics Promise.all functionality
 * @param {Array} promises - Array of promises
 * @returns {Promise} A promise that resolves with an array of results or rejects with the first error
 */
export const all = (promises) => {
  // Handle edge cases
  if (!promises) return Promise.resolve();
  if (promises.length === 0) return Promise.resolve([]);

  return new Promise((resolve, reject) => {
    const results = new Array(promises.length);
    let resolvedCount = 0;
    let rejected = false;

    promises.forEach((promise, index) => {
      Promise.resolve(promise)
        .then(value => {
          if (!rejected) {
            results[index] = value;
            resolvedCount++;
            
            if (resolvedCount === promises.length) {
              resolve(results);
            }
          }
        })
        .catch(error => {
          if (!rejected) {
            rejected = true;
            reject(error);
          }
        });
    });
  });
};

/**
 * Mimics Promise.allSettled functionality
 * @param {Array} promises - Array of promises
 * @returns {Promise} A promise that always resolves with an array of results/errors
 */
export const allSettled = (promises) => {
  // Handle edge cases
  if (!promises) return Promise.resolve();
  if (promises.length === 0) return Promise.resolve([]);

  return new Promise((resolve) => {
    const results = new Array(promises.length);
    let settledCount = 0;

    promises.forEach((promise, index) => {
      Promise.resolve(promise)
        .then(value => {
          results[index] = value;
          settledCount++;
          
          if (settledCount === promises.length) {
            resolve(results);
          }
        })
        .catch(error => {
          results[index] = error;
          settledCount++;
          
          if (settledCount === promises.length) {
            resolve(results);
          }
        });
    });
  });
};

/**
 * Mimics Promise.race functionality
 * @param {Array} promises - Array of promises
 * @returns {Promise} A promise that resolves/rejects with the first settled promise
 */
export const race = (promises) => {
  // Handle edge cases
  if (!promises) return Promise.resolve();
  if (promises.length === 0) return Promise.resolve([]);

  return new Promise((resolve, reject) => {
    promises.forEach(promise => {
      Promise.resolve(promise)
        .then(resolve)
        .catch(reject);
    });
  });
};

/**
 * Mimics Promise.any functionality
 * @param {Array} promises - Array of promises
 * @returns {Promise} A promise that resolves with the first fulfilled promise or rejects with an array of errors
 */
export const any = (promises) => {
  // Handle edge cases
  if (!promises) return Promise.resolve();
  if (promises.length === 0) return Promise.resolve([]);

  return new Promise((resolve, reject) => {
    const errors = new Array(promises.length);
    let rejectedCount = 0;
    let resolved = false;

    promises.forEach((promise, index) => {
      Promise.resolve(promise)
        .then(value => {
          if (!resolved) {
            resolved = true;
            resolve(value);
          }
        })
        .catch(error => {
          errors[index] = error;
          rejectedCount++;
          
          if (rejectedCount === promises.length) {
            reject(errors);
          }
        });
    });
  });
};
