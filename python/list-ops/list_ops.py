def append(list1, list2):
    result = []
    for item in list1:
        result = result + [item]
    for item in list2:
        result = result + [item]
    return result


def concat(lists):
    result = []
    for list_item in lists:
        for item in list_item:
            result = result + [item]
    return result


def filter(function, list):
    result = []
    for item in list:
        if function(item):
            result = result + [item]
    return result


def length(list):
    count = 0
    for _ in list:
        count += 1
    return count


def map(function, list):
    result = []
    for item in list:
        result = result + [function(item)]
    return result


def foldl(function, list, initial):
    result = initial
    for item in list:
        result = function(result, item)
    return result


def foldr(function, list, initial):
    result = initial
    # Use our own reverse function to process the list from right to left
    reversed_list = reverse(list)
    for item in reversed_list:
        result = function(result, item)
    return result


def reverse(list):
    result = []
    # Manual implementation without using range
    i = length(list)
    while i > 0:
        i -= 1
        result = result + [list[i]]
    return result
